package org.ninestar.crawling.data;


import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public class HttpArticle implements Serializable {


    private String source;

    private String language;

    private String url;

    private String title;

    private String text;

    private String textSignature;

    private List<String> appIds;

    private DateTime published;

    private DateTime discovered;

    private String categories;

    private String  description;

    private String  language_code;

    private String meta_keywords;

    public String getMeta_keywords() {
        return meta_keywords;
    }

    public void setMeta_keywords(String meta_keywords) {
        this.meta_keywords = meta_keywords;
    }

    private String images_url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public String getImages_url() {
        return images_url;
    }

    public void setImages_url(String images_url) {
        this.images_url = images_url;
    }




    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public DateTime getPublished() {
        return published;
    }

    public void setPublished(DateTime published) {
        this.published = published;
    }

    public DateTime getDiscovered() {
        return discovered;
    }

    public void setDiscovered(DateTime discovered) {
        this.discovered = discovered;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAppIds() {
        return appIds;
    }

    public void setAppIds(List<String> appIds) {
        this.appIds = appIds;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTextSignature() {
        return textSignature;
    }

    public void setTextSignature(String textSignature) {
        this.textSignature = textSignature;
    }

    @Override
    public String toString() {




        return "HttpArticle{" +
                "source='" + source + '\'' +
                ", language='" + language + '\'' +
                ", language_code='" + language_code + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", meta_keywords='" + meta_keywords + '\'' +
                ", images_url='" + images_url + '\'' +
                ", text='" + text + '\'' +
                ", textSignature='" + textSignature + '\'' +
                ", appIds=" + appIds +
                ", published=" + published +
                ", discovered=" + discovered +
                ", categories=" + categories +
                '}';
    }
}