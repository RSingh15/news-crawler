package org.ninestar.crawling.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class HttpArticleParseResult implements Serializable {

    private HttpArticle article;

    private List<String> titleMatches;

    private List<String> textMatches;

    private List<String> publishedTexts;

    private List<String> publishedMatches;

    private List<String> descriptionMatches;
    private List<String> metaKWMatches;
    private List<String> articleKWMatches;
    private List<String> imagesMatches;
    private List<String> languagecodeMatches;


    private String publishedPattern;

    public HttpArticleParseResult() {
    }

    public HttpArticleParseResult(HttpArticle article) {
        this.article = article;
    }

    public HttpArticle getArticle() {
        return article;
    }

    public void setArticle(HttpArticle article) {
        this.article = article;
    }

    public List<String> getTitleMatches() {
        return titleMatches != null ? titleMatches : Collections.emptyList();
    }

    public void setTitleMatches(List<String> titleMatches) {
        this.titleMatches = titleMatches;
    }

    public List<String> getTextMatches() {
        return textMatches != null ? textMatches : Collections.emptyList();
    }

    public void setTextMatches(List<String> textMatches) {
        this.textMatches = textMatches;
    }

    public List<String> getPublishedTexts() {
        return publishedTexts != null ? publishedTexts : Collections.emptyList();
    }

    public void setPublishedTexts(List<String> publishedTexts) {
        this.publishedTexts = publishedTexts;
    }

    public List<String> getPublishedMatches() {
        return publishedMatches != null ? publishedMatches : Collections.emptyList();
    }

    public void setPublishedMatches(List<String> publishedMatches) {
        this.publishedMatches = publishedMatches;
    }

    public String getPublishedPattern() {
        return publishedPattern;
    }

    public void setPublishedPattern(String publishedPattern) {
        this.publishedPattern = publishedPattern;
    }

    public List<String> getDescriptionMatches() {
        return descriptionMatches;
    }

    public void setDescriptionMatches(List<String> descriptionMatches) {
        this.descriptionMatches = descriptionMatches;
    }

    public List<String> getMetaKWMatches() {
        return metaKWMatches;
    }

    public void setMetaKWMatches(List<String> metaKWMatches) {
        this.metaKWMatches = metaKWMatches;
    }

    public List<String> getArticleKWMatches() {
        return articleKWMatches;
    }

    public void setArticleKWMatches(List<String> articleKWMatches) {
        this.articleKWMatches = articleKWMatches;
    }

    public List<String> getImagesMatches() {
        return imagesMatches;
    }

    public void setImagesMatches(List<String> imagesMatches) {
        this.imagesMatches = imagesMatches;
    }
    public List<String> getLanguagecodeMatches() {
        return languagecodeMatches;
    }

    public void setLanguagecodeMatches(List<String> languagecodeMatches) {
        this.languagecodeMatches = languagecodeMatches;
    }

}
