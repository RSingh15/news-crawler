package org.ninestar.crawling.parser;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.ninestar.crawling.data.HttpArticle;
import org.ninestar.crawling.data.HttpArticleParseResult;
import org.ninestar.crawling.data.HttpSource;
import org.ninestar.crawling.parser.data.MatchedDate;
import org.ninestar.crawling.parser.data.MatchedString;
import org.ninestar.crawling.parser.urls.UrlExtractor;
import org.ninestar.crawling.parser.utils.JsonLdParser;
import org.ninestar.crawling.parser.utils.TextFilters;
import org.ninestar.crawling.parser.utils.TextProfileSignature;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.util.Collections.*;

public class ArticleExtractor {

    private static TextProfileSignature textProfileSignature = new TextProfileSignature();

    public static HttpArticle extractArticle(String html, String url, HttpSource source, String publishedHint) throws IOException {
        return extractArticleWithDetails(html, url, source, publishedHint).getArticle();
    }

    public static HttpArticleParseResult extractArticleWithDetails(String html, String url, HttpSource source, String publishedHint) throws IOException {
        Document document = Jsoup.parse(html, url);
        HttpArticleParseResult result = new HttpArticleParseResult();
        HttpArticle article = new HttpArticle();
        article.setUrl(UrlExtractor.extract(url, document));
        article.setSource(source.getUrl());
        article.setLanguage(source.getLanguage());
        article.setAppIds(source.getAppIds());
        article.setCategories(source.getName());

        List<String> ldJsons = JsonLdParser.extractJsonLdParts(document);
        JsonLdParser.JsonLdArticle ldJsonArticle = JsonLdParser.parse(ldJsons);

        List<MatchedString> titles = extractTitlesWithJsoup(document, ldJsonArticle, source);
        article.setTitle(titles.stream().map(MatchedString::getValue).collect(Collectors.joining("\n")));
        result.setTitleMatches(titles.stream().map(MatchedString::getMatch).collect(Collectors.toList()));

        String keywords = extractKeywordsWithJsoup(document, ldJsonArticle, source);
        article.setMeta_keywords(keywords);
        result.setMetaKWMatches(Collections.singletonList(keywords));

        String lang = extractLanguageWithJsoup(document);
        article.setLanguage(lang);
        result.setLanguagecodeMatches(Collections.singletonList(lang));
        article.setLanguage_code(lang);

        String images = extractimagesWithJsoup(url);
        article.setImages_url(images);
        result.setImagesMatches(Collections.singletonList(images));

        List<MatchedString> desc = extractDescriptionsWithJsoup(document, ldJsonArticle, source);
        article.setDescription(desc.stream().map(MatchedString::getValue).collect(Collectors.joining("\n")));
        result.setDescriptionMatches(desc.stream().map(MatchedString::getMatch).collect(Collectors.toList()));

        List<MatchedString> texts = extractTextsWithJsoup(document, source);
        article.setText(texts.stream()
                .map(MatchedString::getValue)
                .map(t -> TextFilters.normalizeText(t, source.getTextNormalizers()))
                .collect(Collectors.joining("\n")));
        article.setTextSignature(textProfileSignature.getSignature(article.getText()));
        result.setTextMatches(texts.stream().map(MatchedString::getMatch).distinct().collect(Collectors.toList()));

        List<MatchedDate> publicationDates = extractPublicationDates(html, document, ldJsonArticle, source, publishedHint);
        MatchedDate publicationDate = publicationDates.stream().filter(d -> d.getDate() != null).findFirst().orElse(null);
        article.setPublished(publicationDate != null ? publicationDate.getDate() : null);
        result.setPublishedPattern(publicationDate != null ? publicationDate.getPattern() : null);
        List<String> publishedTexts = publicationDate != null ?
                Lists.newArrayList(publicationDate.getValue()) : publicationDates.stream().map(MatchedDate::getValue).collect(Collectors.toList());
        result.setPublishedTexts(publishedTexts);
        List<String> publishedMatches = publicationDate != null ?
                Lists.newArrayList(publicationDate.getMatch()) : publicationDates.stream().map(MatchedDate::getMatch).collect(Collectors.toList());

        result.setPublishedMatches(publishedMatches);

        result.setArticle(article);
        return result;
    }

    private static List<MatchedDate> extractPublicationDates(String html, Document document,
                                                             JsonLdParser.JsonLdArticle ldJsonArticle,
                                                             HttpSource source, String publishedHint) {
        List<MatchedDate> dates = Lists.newArrayList();
        if (publishedHint != null) {
            dates.add(new MatchedDate(publishedHint, "HINT"));
        }
        for (String selector : source.getDateSelectors()) {
            document.select(selector).forEach(e -> dates.add(new MatchedDate(e.text(), selector)));
        }
        if (ldJsonArticle != null && !Strings.isNullOrEmpty(ldJsonArticle.getDatePublished())) {
            dates.add(new MatchedDate(ldJsonArticle.getDatePublished(), "LD+JSON"));
        }
        dates.addAll(DateParser.extractFromMeta(document));
        dates.addAll(DateParser.extractFromProperties(document));
        return dates.stream()
                .map(d -> DateParser.parse(d, source))
                .filter(d -> d != null)
                .collect(Collectors.toList());
    }

    private static MatchedDate parseDate(MatchedDate matchedText, HttpSource source) {
        return DateParser.parse(matchedText, source);
    }

    private static List<MatchedString> extractTextsWithJsoup(Document document, HttpSource source) {
        List<MatchedString> texts = Lists.newArrayList();
        for (String selector : source.getTextSelectors()) {
            document.select(selector).forEach(e -> texts.add(new MatchedString(e.text(), selector)));
        }
        if (!texts.isEmpty()) {
            return texts;
        }
        String itemPropValue = document.select("[itemprop*=articleBody] p").text();
        if (itemPropValue != null && !itemPropValue.trim().isEmpty()) {
            return Lists.newArrayList(new MatchedString(itemPropValue, "[itemprop*=articleBody] p"));
        }
        return document.select("p").stream()
                .map(e -> new MatchedString(e.text(), "p"))
                .collect(Collectors.toList());
    }

    private static List<MatchedString> extractTitlesWithJsoup(Document document,
                                                              JsonLdParser.JsonLdArticle ldJsonArticle,
                                                              HttpSource source) {
        List<MatchedString> titles = Lists.newArrayList();
        if (source.getTitleSelectors().size() > 0) {
            for (String selector : source.getTitleSelectors()) {
                document.select(selector).forEach(e -> titles.add(new MatchedString(e.text(), selector)));
            }
        } else {
            if (ldJsonArticle != null && Strings.isNullOrEmpty(ldJsonArticle.getHeadline())) {
                titles.add(new MatchedString(ldJsonArticle.getHeadline(), "LD+JSON"));
            }
            titles.addAll(TitleParser.extractFromMeta(document));
        }
        if (titles.isEmpty()) {
            titles.addAll(document.select("h1").stream().map(e -> new MatchedString(e.text(), "h1")).collect(Collectors.toList()));
            titles.addAll(document.select("title").stream().map(e -> new MatchedString(e.text(), "title")).collect(Collectors.toList()));
        }
        return titles.stream()
                .map(mv -> {
                    //Drop endings like ' | Reuters.com'
                    mv.setValue(mv.getValue().replaceAll("\\s*\\|.+", ""));
                    return mv;
                })
                .filter(mv -> !mv.getValue().contains("${")) //Drop titles which contain variables
                .distinct()
                .limit(1)
                .collect(Collectors.toList());
    }

    private static String extractKeywordsWithJsoup(Document document,
                                                              JsonLdParser.JsonLdArticle ldJsonArticle,
                                                              HttpSource source) {
        List<String> keywords = Lists.newArrayList();
        if (source.getKeywordsSelector().size() > 0) {
            for (String selector : source.getKeywordsSelector()) {
                document.select(selector).forEach(e -> keywords.add(String.valueOf(new MatchedString(e.text(), selector))));
            }
        } else {
            if (ldJsonArticle != null && Strings.isNullOrEmpty(ldJsonArticle.getMetakw())) {
                keywords.add(String.valueOf(new MatchedString(ldJsonArticle.getMetakw(), "LD+JSON")));
            }
            keywords.add(KeyWordParser.extractFromMeta(document));
        }

        return keywords.toString();
    }

    private static String extractLanguageWithJsoup(Document document) throws IOException {
    return Language_code.extractlanguage(document);

    }
    private static List<MatchedString> extractDescriptionsWithJsoup(Document document,
                                                                    JsonLdParser.JsonLdArticle ldJsonArticle,
                                                                    HttpSource source) {
        List<MatchedString> description = Lists.newArrayList();
        if (source.getdescriptionSelector().size() > 0) {
            for (String selector : source.getdescriptionSelector()) {
                document.select(selector).forEach(e -> description.add(new MatchedString(e.text(), selector)));
            }
        } else {
            if (ldJsonArticle != null && Strings.isNullOrEmpty(ldJsonArticle.getDescriptionBody())) {
                description.add(new MatchedString(ldJsonArticle.getDescriptionBody(), "LD+JSON"));
            }
            description.addAll(DescriptionParser.extractFromMeta(document));
        }
        if (description.isEmpty()) {
//            description.addAll(document.select("h3").stream().map(e -> new MatchedString(e.text(), "h3")).collect(Collectors.toList()));
            description.addAll(document.select("description").stream().map(e -> new MatchedString(e.text(), "description")).collect(Collectors.toList()));
        }
        return description.stream()
                .map(mv -> {
                    //Drop endings like ' | Reuters.com'
                    mv.setValue(mv.getValue().replaceAll("\\s*\\|.+", ""));
                    return mv;
                })
                .filter(mv -> !mv.getValue().contains("${")) //Drop Description which contain variables
                .distinct()
                .limit(1)
                .collect(Collectors.toList());
    }
    private static String extractimagesWithJsoup(String url) throws IOException {
    String image =ImageLinkParser.extractImageUrl(url);
    return image;
}

}
