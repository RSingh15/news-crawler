package org.ninestar.crawling.parser;

import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;

import java.util.List;

public class KeyWordParser {


    public static String extractFromMeta(Document document) {


        String KEYWORDS_META_KEYS ="";

        try {
            KEYWORDS_META_KEYS = document.select("meta[name=keywords]").first().attr("content");
        }catch (Exception e){}finally {
            if (KEYWORDS_META_KEYS.isEmpty()) {
                KEYWORDS_META_KEYS = document.select("[itemprop*=keywords]").attr("content");
                if (KEYWORDS_META_KEYS.isEmpty()) {
                    KEYWORDS_META_KEYS = "news";
                }
            }
        }
        return KEYWORDS_META_KEYS;
    }}
