package org.ninestar.crawling.parser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;


class ImageLinkParser {

//    public static void main(String[] args) throws IOException {
//        String a= extractImageUrl("https://www.huffingtonpost.in/2018/09/13/yami-gautam-picks-the-5-shows-you-need-to-watch-on-netflix-this-weekend_a_23525837/");
//        System.out.println(a);
//    }
    // TODO: Add junit test case for this. (Construct Document from string, extract, check)
    public static String extractImageUrl(String url) throws IOException {
        String contentType = new URL(url).openConnection().getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return url;
            }
        }

        Document document = Jsoup.connect(url).get();

        JSONObject Images = new JSONObject();
        Images.put("FromTwitter",getImageFromTwitterShared(document));
        Images.put("Fromhreflink",getImageFromLinkRel(document));
        Images.put("OpenGraph",getImageFromOpenGraph(document));
        Images.put("Schema",getImageFromSchema(document));
        Images.put("Imgsrc",getImageFromImgSrc(document));
        Images.put("TwieeterCard",getImageFromTwitterCard(document));


//        System.out.println(Images);
        return Images.toString();



    }


  private static JSONArray getImageFromOpenGraph(Document document) {
      JSONArray fromOpenGrap = new JSONArray();
      Elements image = document.select("meta[property=og:image]");
      for (Element img : image) {
          if (img != null) {
              fromOpenGrap.put(img.attr("abs:content"));

          }
      }
      Elements secureImage = document.select("meta[property=og:image:secure]");
      for (Element imgsecure : secureImage)
          if (imgsecure != null) {
              fromOpenGrap.put(imgsecure.attr("abs:content"));

          }
      return fromOpenGrap;
  }

    private static JSONArray getImageFromTwitterShared(Document document) {
        JSONArray TwitterShared = new JSONArray();
        Elements divClass = document.select("div.media-gallery-image-wrapper");
        for (Element div : divClass) {
            if (div == null) {
                return null;
            } else {
                Elements images = div.select("img.media-slideshow-image");
                for (Element img : images) {
                    if (img != null) {
                        TwitterShared.put(img.absUrl("src"));
                    }
                }
            }
        }

        return TwitterShared;
    }

    private static JSONArray getImageFromLinkRel(Document document) {
        JSONArray Link = new JSONArray();
        Elements link = document.select("link[rel=image_src]");
        for (Element lin : link) {
            if (lin != null) {
                Link.put(lin.attr("abs:href"));
            }

        }     return Link; }
//
        private static JSONArray getImageFromTwitterCard (Document document){
            JSONArray TweetCard = new JSONArray();
            Elements meta = document.select("meta[name=twitter:card][content=photo]");
            for (Element met:meta) {
                if (met == null) {
                    return null;
                }}
            Elements image = document.select("meta[name=twitter:image]");
                for(Element img:image){
                    TweetCard.put(img.attr("abs:content"));
                }
            return TweetCard;
        }



        private static JSONArray getImageFromSchema (Document document){
            JSONArray Schema = new JSONArray();
            Elements containers = document.select("*[itemscope][itemtype=http://schema.org/ImageObject]");
            for(Element container:containers)
            {
                if (container == null) {
                return null;
                }else{
                    Elements image = container.select("img[itemprop=contentUrl]");
                    for(Element img:image){
                        if (img == null) {
                            return null;}
                            else {Schema.put(img.absUrl("src"));}

                        }
                }
            }
return Schema;
        }

    private static JSONArray getImageFromImgSrc (Document document){
        JSONArray source = new JSONArray();

        Elements img = document.select("img[src~=(?i)\\.(jpg|jpe?g|bmp)]");

        for (Element element : img) {

        source.put((element.attr("src")));

//        System.out.println(element.attr("src"));
    }
        return source;
}
    }