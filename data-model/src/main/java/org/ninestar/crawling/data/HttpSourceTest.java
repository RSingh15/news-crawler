package org.ninestar.crawling.data;

import java.io.Serializable;

public class HttpSourceTest implements Serializable {

    private String source;

    private String url;

    private Boolean urlAccepted;

    private String html;

    private String title;

    private String text;

    private String date;

    private String description;
    private String metakeyword;
    private String articlekeyword;
    private String imageurls;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetakeyword() {
        return metakeyword;
    }

    public void setMetakeyword(String metakeyword) {
        this.metakeyword = metakeyword;
    }

    public String getArticlekeyword() {
        return articlekeyword;
    }

    public void setArticlekeyword(String articlekeyword) {
        this.articlekeyword = articlekeyword;
    }

    public String getImageurls() {
        return imageurls;
    }

    public void setImageurls(String imageurls) {
        this.imageurls = imageurls;
    }




    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getUrlAccepted() {
        return urlAccepted;
    }

    public void setUrlAccepted(Boolean urlAccepted) {
        this.urlAccepted = urlAccepted;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
