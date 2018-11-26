package org.ninestar.crawling.parser;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsoup.nodes.Document;
import org.ninestar.crawling.parser.data.MatchedString;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DescriptionParser {

        private static final List<String> DECRIPTION_META_KEYS = Lists.newArrayList("og:description");

        public static List<MatchedString> extractFromMeta(Document document) {
            String itempropValue = document.select("[itemprop*=description]").text();
            if (itempropValue != null && !itempropValue.trim().isEmpty()) {
                return Lists.newArrayList(new MatchedString(itempropValue, "[itemprop*=description]"));
            }
            Map<String, String> metaValues = Maps.newHashMap();
            document.select("meta").forEach(m -> {
                String name = m.attr("name");
                String property = m.attr("property");
                String content = m.attr("content");
                if (!Strings.isNullOrEmpty(name)) {
                    metaValues.put(name.toLowerCase(), content);
                } else if (!Strings.isNullOrEmpty(property)) {
                    metaValues.put(property.toLowerCase(), content);
                }
            });
            return DECRIPTION_META_KEYS.stream()
                    .filter(k -> metaValues.get(k) != null)
                    .map(k -> new MatchedString(metaValues.get(k), "META:" + k))
                    .collect(Collectors.toList());
        }
    }