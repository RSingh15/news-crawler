package org.ninestar.crawling.parser.utils;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class JsonLdParser {

    private static final Logger LOG = LoggerFactory.getLogger(JsonLdParser.class);

    private static final String SCHEMA_CLASS_ARTICLE = "http://schema.org/Article";
    private static final String SCHEMA_CLASS_NEWS_ARTICLE = "http://schema.org/NewsArticle";
    private static final String SCHEMA_ATTR_ARTICLE = "http://schema.org/article";
    private static final String SCHEMA_ATTR_ARTICLE_BODY = "http://schema.org/articleBody";
    private static final String SCHEMA_ATTR_HEADLINE = "http://schema.org/headline";
    private static final String SCHEMA_ATTR_PUBLISHED = "http://schema.org/datePublished";
    private static final String SCHEMA_ATTR_DESCRIPTION = "https://schema.org/description";
    private static final String SCHEMA_ATTR_KEYWORDS = "https://schema.org/keywords";
    private static final String SCHEMA_ATTR_LANGUAGE = "http://schema.org/inLanguage";
    private static final String SCHEMA_ATTR_IMAGE = "https://schema.org/image";



    public static JsonLdArticle parse(List<String> jsons) {
        try {
            for (String jsonLd : jsons) {
                if (!Strings.isNullOrEmpty(jsonLd)) {
                    jsonLd = jsonLd.replaceAll("http://www\\.schema\\.org", "http://schema.org");
                    jsonLd = jsonLd.replaceAll("\"http://schema\\.org\"", "\"http://schema.org/\"");
                    Object jsonObject = JsonUtils.fromString(jsonLd);
                    Map<String, Object> data = JsonLdProcessor.compact(jsonObject, new HashMap(), new JsonLdOptions());
                    String type = Objects.toString(data.get("@type"), null);
                    if (type == null && data.get(SCHEMA_ATTR_ARTICLE) instanceof Map) {
                        data = (Map<String, Object>) data.get(SCHEMA_ATTR_ARTICLE);
                        type = Objects.toString(data.get("@type"), null);
                    }
                    if (SCHEMA_CLASS_ARTICLE.equalsIgnoreCase(type) || SCHEMA_CLASS_NEWS_ARTICLE.equalsIgnoreCase(type)) {
                        JsonLdArticle article = new JsonLdArticle();
                        String headline = Objects.toString(data.get(SCHEMA_ATTR_HEADLINE), null);
                        if (!Strings.isNullOrEmpty(headline)) {
                            headline = StringEscapeUtils.unescapeHtml4(headline);
                        }
                        article.setHeadline(headline);
                        String articleBody = Objects.toString(data.get(SCHEMA_ATTR_ARTICLE_BODY), null);
                        if (!Strings.isNullOrEmpty(articleBody)) {
                            articleBody = StringEscapeUtils.unescapeHtml4(articleBody);
                        }
                        article.setArticleBody(articleBody);
                        String descriptionBody = Objects.toString(data.get(SCHEMA_ATTR_DESCRIPTION), null);
                        if (!Strings.isNullOrEmpty(descriptionBody)) {
                            descriptionBody = StringEscapeUtils.unescapeHtml4(descriptionBody);
                        }
                        article.setDescriptionBody(descriptionBody);
                        String Language = Objects.toString(data.get(SCHEMA_ATTR_LANGUAGE), null);
                        if (!Strings.isNullOrEmpty(Language)) {
                            Language = StringEscapeUtils.unescapeHtml4(Language);
                        }
                        article.setLanguage(Language);
                        Object publishedObject = data.get(SCHEMA_ATTR_PUBLISHED);
                        String datePublished = null;
                        if (publishedObject instanceof String) {
                            datePublished = (String) publishedObject;
                        } else if (publishedObject instanceof Map) {
                            datePublished = Objects.toString(((Map) publishedObject).get("@value"), null);
                        }
                        article.setDatePublished(datePublished);

                        Object ImageObject = data.get(SCHEMA_ATTR_IMAGE);
                        String imageurl = null;
                        if (ImageObject instanceof String) {
                            imageurl = (String) ImageObject;
                        } else if (publishedObject instanceof Map) {
                            imageurl = Objects.toString(((Map) ImageObject).get("url"), null);
                        }
                        article.setImage(imageurl);

                        String keywords = Objects.toString(data.get(SCHEMA_ATTR_KEYWORDS), null);
                        if (!Strings.isNullOrEmpty(keywords)) {
                            keywords = StringEscapeUtils.unescapeHtml4(keywords);
                        }
                        article.setMetakw(keywords);
                        return article;
                    }
                }
            }
        } catch (Exception e) {
            LOG.warn("Failed to parse ld+json", e);
        }
        return null;
    }


    public static List<String> extractJsonLdParts(Document document) {
        Elements elements = document.select("script[type=\"application/ld+json\"]");
        return elements.stream()
            .map(Element::html)
            .collect(Collectors.toList());
    }

    public static class JsonLdArticle {

        private String headline;

        private String datePublished;

        private String articleBody;

        private String descriptionBody;

        private String metakw;

        private String articlekw;

        private String language;

        private String Image;

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String image) {
            Image = image;
        }

        public String getDescriptionBody() {
            return descriptionBody;
        }

        public void setDescriptionBody(String descriptionBody) {
            this.descriptionBody = descriptionBody;
        }

        public String getMetakw() {
            return metakw;
        }

        public void setMetakw(String metakw) {
            this.metakw = metakw;
        }

        public String getArticlekw() {
            return articlekw;
        }

        public void setArticlekw(String articlekw) {
            this.articlekw = articlekw;
        }




        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public String getDatePublished() {
            return datePublished;
        }

        public void setDatePublished(String datePublished) {
            this.datePublished = datePublished;
        }

        public String getArticleBody() {
            return articleBody;
        }

        public void setArticleBody(String articleBody) {
            this.articleBody = articleBody;
        }
    }

}
