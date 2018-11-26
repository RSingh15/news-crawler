package org.ninestar.crawling.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Language_code {

    public static String extractlanguage(Document document) throws IOException {

        String LANGUAGE_CODE = "";

        try {
            LANGUAGE_CODE = document.select("meta[property=og:locale]").attr("content");
        } catch (Exception e) {
        } finally {
            if (LANGUAGE_CODE.isEmpty()) {
                LANGUAGE_CODE = document.select("[http-equiv*=content-language]").attr("content");
                if (LANGUAGE_CODE.isEmpty()) {
                    LANGUAGE_CODE = document.select("[http-equiv*=Content-Language]").attr("content");

                }
            }
        }
        return LANGUAGE_CODE;
    }
}