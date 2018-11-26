/*

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

*/
/**
 * Given a url to a web page, extract a suitable image from that page. This will
 * attempt to follow a method similar to Google+, as described <a href=
 * "http://webmasters.stackexchange.com/questions/25581/how-does-google-plus-select-an-image-from-a-shared-link"
 * >here</a>
 *
 *//*

public class trail {
    public static void main(String[] args) throws IOException {
//        String<List> image = extractImageUrl("https://unsplash.com/t/wallpapers");
        System.out.println(extractImageUrl("https://unsplash.com/t/wallpapers"));
    }

    // TODO: Add junit test case for this. (Construct Document from string, extract, check)
    public static List<String> extractImageUrl(String url) throws IOException {
        String contentType = new URL(url).openConnection().getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return Collections.singletonList(url);
            }
        }

        Document document = Jsoup.connect(url).get();

        */
/*String imageUrl = null;

        imageUrl = getImageFromSchema(document);
        if (imageUrl != null) {
            return imageUrl;
        }

        imageUrl = getImageFromOpenGraph(document);
        if (imageUrl != null) {
            return imageUrl;
        }

        imageUrl = getImageFromTwitterCard(document);
        if (imageUrl != null) {
            return imageUrl;
        }

        imageUrl = getImageFromTwitterShared(document);
        if (imageUrl != null) {
            return imageUrl;
        }

        imageUrl = getImageFromLinkRel(document);
        if (imageUrl != null) {
            return imageUrl;
        }

        imageUrl = getImageFromGuess(document);
        if (imageUrl != null) {
            return imageUrl;
        }
*//*

        List<String> imageUrl = new ArrayList<String>();

        imageUrl.add(getImageFromTwitterShared(document));
        imageUrl.add(getImageFromGuess(document));
        imageUrl.add(getImageFromLinkRel(document));
        imageUrl.add(getImageFromOpenGraph(document));
        imageUrl.add(getImageFromSchema(document));
        imageUrl.add(getImageFromTwitterCard(document));

        return imageUrl;
    }

    private static String getImageFromTwitterShared(Document document) {
        Element div = document.select("div.media-gallery-image-wrapper").first();
        if (div == null) {
            return null;
        }
        Element img = div.select("img.media-slideshow-image").first();
        if (img != null) {
            return img.absUrl("src");
        }
        return null;
    }

    private static String getImageFromGuess(Document document) {
        // TODO
        return null;
    }

    private static String getImageFromLinkRel(Document document) {
        Element link = document.select("link[rel=image_src]").first();
        if (link != null) {
            return link.attr("abs:href");
        }
        return null;
    }

    private static String getImageFromTwitterCard(Document document) {
        Element meta = document.select("meta[name=twitter:card][content=photo]").first();
        if (meta == null) {
            return null;
        }
        Element image = document.select("meta[name=twitter:image]").first();
        return image.attr("abs:content");
    }

    private static String getImageFromOpenGraph(Document document) {
        Elements image = document.select("meta[property=og:image]").first();
        for (Element img : image) {
            if (img != null) {
            return img.attr("abs:content");
        }
        Element secureImage = document.select("meta[property=og:image:secure]").first();
        if (secureImage != null) {
            return secureImage.attr("abs:content");
        }}
        return null;
    }

    private static String getImageFromSchema(Document document) {
        Element container =
                document.select("*[itemscope][itemtype=http://schema.org/ImageObject]").first();
        if (container == null) {
            return null;
        }

        Element image = container.select("img[itemprop=contentUrl]").first();
        if (image == null) {
            return null;
        }
        return image.absUrl("src");
    }
}*/
