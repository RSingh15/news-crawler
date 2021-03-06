package org.ninestar.crawling.es;

import org.ninestar.crawling.data.HttpSource;
import org.ninestar.crawling.data.PageableList;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class EsHttpSourceOperationsTest {

    @Test
    @Ignore
    public void test() {
        ElasticConnection connection = ElasticConnection.getConnection("localhost", 9200, "http");
        EsHttpSourceOperations esHttpSourceOperations = new EsHttpSourceOperations(connection, "demo-http_sources", "http_source");
        PageableList<HttpSource> data = esHttpSourceOperations.filter(null);
        for (HttpSource source : data.getItems()) {
            System.out.println(">>" + source);
        }
    }

    @Ignore
    @Test
    public void testRefresh() {
        ElasticConnection connection = ElasticConnection.getConnection("localhost", 9200, "http");
        EsHttpSourceOperations esHttpSourceOperations = new EsHttpSourceOperations(connection, "cf-http_sources", "http_source");
        HttpSource source = new HttpSource();
        source.setName("test");
        source.setUrl("url");
        esHttpSourceOperations.save(source);
        String currentName = esHttpSourceOperations.get("url").getName();
        assertEquals("test", currentName);
        source.setName("new name");
        esHttpSourceOperations.save(source);
        String name = esHttpSourceOperations.get("url").getName();
        assertNotEquals("test", name);
    }
}
