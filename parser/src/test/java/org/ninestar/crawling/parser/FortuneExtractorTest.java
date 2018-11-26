package org.ninestar.crawling.parser;

import org.ninestar.crawling.data.HttpArticle;
import org.ninestar.crawling.data.HttpSource;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class FortuneExtractorTest extends BaseArticleExtractorTest {

    @Test
    public void testFortune1() throws Exception {
        String html = loadArticle("fortune1");
        String url = "http://fortune.com/2017/04/13/susan-fowler-uber-editor-stripe/";
        HttpArticle article = ArticleExtractor.extractArticle(html, url, fortuneSource(), "2017/04/13");
        assertEquals("2017-04-13T00:00:00.000Z", article.getPublished().toInstant().toString());
    }

    private HttpSource fortuneSource() {
        HttpSource source = new HttpSource();
        return source;
    }
}
