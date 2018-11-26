package org.ninestar.crawling.crawler.spout;

import org.ninestar.crawling.crawler.DefaultServiceProvider;
import org.junit.Test;

public class UrlGeneratorSpoutTest {


    @Test
    public void test() {
        UrlGeneratorSpout spout = new UrlGeneratorSpout(new DefaultServiceProvider());
    }

}
