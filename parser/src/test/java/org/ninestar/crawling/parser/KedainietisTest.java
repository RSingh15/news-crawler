package org.ninestar.crawling.parser;

import org.ninestar.crawling.data.HttpArticle;
import org.ninestar.crawling.data.HttpArticleParseResult;
import org.ninestar.crawling.data.HttpSource;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class KedainietisTest extends BaseArticleExtractorTest{

    private HttpSource kedainietisSource() {
        HttpSource source = new HttpSource();
        source.setLanguage("lt");
        source.setDateSelectors(Arrays.asList("span.dtreviewed"));
        return source;
    }

    @Test
    public void testKedainietis() throws Exception {
        String html = loadArticle("kedainietis");
        String url = "http://www.kedainietis.lt/naujienos/naujienos/nedeklaravus-gyvenamosios-vietos-nepasieks-ir-sodros-mokami-alimentai-17694/";
        HttpArticleParseResult parseResult = ArticleExtractor.extractArticleWithDetails(html, url, kedainietisSource(), null);
        HttpArticle article = parseResult.getArticle();
        assertEquals("Nedeklaravus gyvenamosios vietos, nepasieks ir „Sodros“ mokami alimentai".trim(), article.getTitle().trim());
        assertTrue(article.getText().contains("valstybės biudžeto Lietuvoje"));
        assertTrue(article.getText().startsWith("Iš valstybės"));
        assertEquals(parseResult.getPublishedMatches().get(0), "span.dtreviewed");
        assertNotNull(article.getPublished());
    }

}
