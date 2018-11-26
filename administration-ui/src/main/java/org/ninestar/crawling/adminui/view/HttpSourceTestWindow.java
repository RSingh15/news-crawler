package org.ninestar.crawling.adminui.view;


import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.ninestar.crawling.adminui.HttpSourceTestsCache;
import org.ninestar.crawling.adminui.HttpSourceTestsCache.HttpSourceTest;
import org.ninestar.crawling.commonui.ElasticSearch;
import org.ninestar.crawling.data.DataUtils;
import org.ninestar.crawling.data.HttpArticle;
import org.ninestar.crawling.data.HttpArticleParseResult;
import org.ninestar.crawling.data.HttpSource;
import org.ninestar.crawling.parser.ArticleExtractor;
import org.ninestar.crawling.parser.urls.UrlFilters;

import java.io.IOException;

public class HttpSourceTestWindow extends Window {

    private HttpSource source;

    private TextField urlField = new TextField("URL");
    private TextArea htmlField = new TextArea("HTML");
    private Button parseButton = new Button("Parse", (event) -> {
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    private Button addToTestsButton = new Button("Add To Tests", (event) -> this.addToTests());

    private Button cancelButton = new Button("Cancel", (event) -> this.close());

    private TextField normalizedUrlField = new TextField("Normalized URL");
    private TextArea normalizersField = new TextArea("Normalizers Used");
    private CheckBox acceptedField = new CheckBox("URL Accepted");
    private TextField urlFilterField = new TextField("Matched Filter");
    private TextField titleField = new TextField("Extracted Title");
    private TextArea titleSelectorsField = new TextArea("Title Selectors");
    private TextArea textField = new TextArea("Extracted Text");
    private TextArea textSelectorsField = new TextArea("Text Selectors");
    private TextField dateField = new TextField("Extracted Date");
    private TextField datePatternField = new TextField("Date Pattern");
    private TextArea dateSelectorsField = new TextArea("Date Selectors");
    private TextArea dateTextsField = new TextArea("Date Texts");
    private TextArea descriptionField = new TextArea("Extracted Decription");
    private TextArea descriptionSelectorField = new TextArea("Decription Selectors");
    private TextArea meta_keywordsField = new TextArea("Extracted meta_keywords");
    private TextArea meta_keywordselector = new TextArea("Keywords Selector");
//    private TextArea article_keywordsField = new TextArea("Extracted article_keywords");
//    private TextArea article_keywordsSelectorField = new TextArea("article_keywords");
    private TextArea images_urlField = new TextArea("Extracted images_url");
//    private TextArea images_urlSelectorField = new TextArea("images_url Selector");

    public HttpSourceTestWindow(HttpSource source) {
        this.source = source;
        setCaption(String.format(" %s Configuration Test", source.getName()));
        setModal(true);
        setIcon(FontAwesome.COGS);
        center();
        setWidth(80, Unit.PERCENTAGE);
        setHeight(80, Unit.PERCENTAGE);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);

        FormLayout inputForm = new FormLayout();
        inputForm.setMargin(true);
        inputForm.setSpacing(true);

        HttpSourceTest cached = HttpSourceTestsCache.get(source.getUrl());

        urlField.setValue(cached.getUrl());
        urlField.setSizeFull();

        htmlField.setValue(cached.getHtml());
        htmlField.setRows(10);
        htmlField.setSizeFull();

        parseButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

        HorizontalLayout actions = new HorizontalLayout(parseButton, addToTestsButton, cancelButton);
        actions.setSpacing(true);

        inputForm.addComponents(urlField, htmlField, actions);

        mainLayout.addComponent(inputForm);


        FormLayout resultLayout = new FormLayout();
        resultLayout.setMargin(true);
        resultLayout.setSpacing(true);

        normalizedUrlField.setReadOnly(true);
        normalizedUrlField.setSizeFull();
        resultLayout.addComponent(normalizedUrlField);

        normalizersField.setReadOnly(true);
        normalizersField.setSizeFull();
        normalizersField.setRows(2);
        resultLayout.addComponent(normalizersField);

        acceptedField.setReadOnly(true);
        acceptedField.setSizeFull();
        resultLayout.addComponent(acceptedField);

        urlFilterField.setReadOnly(true);
        urlFilterField.setSizeFull();
        resultLayout.addComponent(urlFilterField);

        titleField.setReadOnly(true);
        titleField.setSizeFull();
        resultLayout.addComponent(titleField);

        titleSelectorsField.setReadOnly(true);
        titleSelectorsField.setSizeFull();
        titleSelectorsField.setRows(2);
        resultLayout.addComponent(titleSelectorsField);

        descriptionField.setReadOnly(true);
        descriptionField.setSizeFull();
        resultLayout.addComponent(descriptionField);

        descriptionSelectorField.setReadOnly(true);
        descriptionSelectorField.setSizeFull();
        descriptionSelectorField.setRows(2);
        resultLayout.addComponent(descriptionSelectorField);

        meta_keywordsField.setReadOnly(true);
        meta_keywordsField.setSizeFull();
        resultLayout.addComponent(meta_keywordsField);

        meta_keywordselector.setReadOnly(true);
        meta_keywordselector.setSizeFull();
        meta_keywordselector.setRows(2);
        resultLayout.addComponent(meta_keywordselector);
/*
        article_keywordsField.setReadOnly(true);
        article_keywordsField.setSizeFull();
        resultLayout.addComponent(article_keywordsField);*/

       /* article_keywordsSelectorField.setReadOnly(true);
        article_keywordsSelectorField.setSizeFull();
        article_keywordsSelectorField.setRows(2);
        resultLayout.addComponent(article_keywordsSelectorField);*/

        images_urlField.setReadOnly(true);
        images_urlField.setSizeFull();
        resultLayout.addComponent(images_urlField);

        dateField.setReadOnly(true);
        dateField.setSizeFull();
        resultLayout.addComponent(dateField);

        datePatternField.setReadOnly(true);
        datePatternField.setSizeFull();
        resultLayout.addComponent(datePatternField);

        dateSelectorsField.setReadOnly(true);
        dateSelectorsField.setSizeFull();
        dateSelectorsField.setRows(2);
        resultLayout.addComponent(dateSelectorsField);

        dateTextsField.setReadOnly(true);
        dateTextsField.setSizeFull();
        dateTextsField.setRows(2);
        resultLayout.addComponent(dateTextsField);

        textField.setReadOnly(true);
        textField.setSizeFull();
        textField.setRows(5);
        resultLayout.addComponent(textField);

        textSelectorsField.setReadOnly(true);
        textSelectorsField.setSizeFull();
        textSelectorsField.setRows(2);
        resultLayout.addComponent(textSelectorsField);

        mainLayout.addComponent(resultLayout);

        setContent(mainLayout);
    }

    private void parse() throws IOException {
        String url = urlField.getValue();
        UrlFilters urlFilters = UrlFilters.create(source.getUrlNormalizers(), source.getUrlFilters());
        UrlFilters.FilteringResult filteringResult = urlFilters.filterWithDetails(url);
        setReadOnlyValue(normalizedUrlField, filteringResult.getNormalized());
        setReadOnlyValue(normalizersField, Joiner.on("\n").join(filteringResult.getNormalizers()));
        setReadOnlyValue(acceptedField, filteringResult.getAccepted());
        setReadOnlyValue(urlFilterField, Strings.nullToEmpty(filteringResult.getFilter()));

        String html = Strings.nullToEmpty(htmlField.getValue()).trim();
        HttpArticleParseResult parseResult = html.isEmpty() ? new HttpArticleParseResult(new HttpArticle()) : ArticleExtractor.extractArticleWithDetails(html, url, source, null);
        HttpArticle article = parseResult.getArticle();
        setReadOnlyValue(titleField, article.getTitle());
        setReadOnlyValue(titleSelectorsField, Joiner.on("\n").join(parseResult.getTitleMatches()));
        setReadOnlyValue(dateField, Strings.nullToEmpty(DataUtils.formatInUTC(article.getPublished())));
        setReadOnlyValue(datePatternField, Strings.nullToEmpty(parseResult.getPublishedPattern()));
        setReadOnlyValue(dateSelectorsField, Joiner.on("\n").join(parseResult.getPublishedMatches()));
        setReadOnlyValue(dateTextsField, Joiner.on("\n").join(parseResult.getPublishedTexts()));
        setReadOnlyValue(textField, article.getText());
        setReadOnlyValue(textSelectorsField, Joiner.on("\n").join(parseResult.getTextMatches()));
        setReadOnlyValue(descriptionField,article.getDescription());
        setReadOnlyValue(descriptionSelectorField, Joiner.on("\n").join(parseResult.getDescriptionMatches()));
        setReadOnlyValue(meta_keywordsField,article.getMeta_keywords());
        setReadOnlyValue(meta_keywordselector, Joiner.on("\n").join(parseResult.getMetaKWMatches()));
//        setReadOnlyValue(images_urlField,article.getImages_url());
        setReadOnlyValue(images_urlField,Joiner.on("\n").join(parseResult.getImagesMatches()));
        HttpSourceTestsCache.put(this.source.getUrl(), url, html);
    }


    private void addToTests() {
        org.ninestar.crawling.data.HttpSourceTest hst = new org.ninestar.crawling.data.HttpSourceTest();
        hst.setSource(source.getUrl());
        hst.setUrl(urlField.getValue());
        hst.setHtml(htmlField.getValue());
        hst.setUrlAccepted(acceptedField.getValue());
        hst.setTitle(titleField.getValue());
        hst.setText(textField.getValue());
        hst.setDate(dateField.getValue());


        ElasticSearch.getHttpSourceTestOperations().save(hst);
        Notification.show(String.format("Test for '%s' added successfully!", urlField.getValue()));
    }


    private <T> void setReadOnlyValue(Field<T> field, T value) {
        field.setReadOnly(false);
        field.setValue(value);
        field.setReadOnly(true);
    }
}
