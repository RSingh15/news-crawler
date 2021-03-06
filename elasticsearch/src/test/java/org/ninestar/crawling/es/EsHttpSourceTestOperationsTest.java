package org.ninestar.crawling.es;

import org.ninestar.crawling.data.HttpSourceTest;
import org.ninestar.crawling.data.PageableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EsHttpSourceTestOperationsTest {

    private static final Logger LOG = LoggerFactory.getLogger(EsHttpSourceTestOperationsTest.class);

    private static final String ES_TEST_HOST = "127.0.0.1";
    private static final int ES_REST_TEST_PORT = 9205;
    private static final String ES_REST_TEST_SCHEME = "http";
    private static final int ES_TRANSPORT_TEST_PORT = 9305;
    private static final String ES_DATA_DIRECTORY = "target/elasticsearch-data";
    private static final boolean ES_CLEAN_DATA_DIR = true;
    private static final String INDEX_ALIAS = "http_source_tests";
    private static final String DOC_TYPE = "http_source_test";
    private static final String INDEX_CONF_RESOURCE_FILE = "indices/http_source_test.json";

    private ElasticsearchTestServer elasticsearchTestServer;

    @Before // setup()
    public void before() throws Exception {
        LOG.info("Setting ES server up!");
        this.elasticsearchTestServer = ElasticsearchTestServer.builder()
                .httpPort(ES_REST_TEST_PORT)
                .transportPort(ES_TRANSPORT_TEST_PORT)
                .dataDirectory(ES_DATA_DIRECTORY)
                .cleanDataDir(ES_CLEAN_DATA_DIR)
                .build();
        this.elasticsearchTestServer.start();

        String indexConf = TestUtils.readResourceAsString(INDEX_CONF_RESOURCE_FILE);
        new IndexManager(ES_TEST_HOST, ES_REST_TEST_PORT).prepare(INDEX_ALIAS, indexConf, true);
    }

    @After
    public void after() throws Exception {
        LOG.info("Tearing ES server down.");
        this.elasticsearchTestServer.stop();
    }

    @Test
    @Ignore
    public void testEsHttpSourceTestOps() throws IOException, URISyntaxException, InterruptedException {
        HttpSourceTest httpSourceTest = new HttpSourceTest();
        httpSourceTest.setUrl("http://www.tokenmill.lt/");
        httpSourceTest.setSource("http://www.tokenmill.lt/");
        httpSourceTest.setHtml(TestUtils.readResourceAsString("www.tokenmill.lt.html"));
        httpSourceTest.setUrlAccepted(true);
        httpSourceTest.setTitle("TokenMill");
        httpSourceTest.setText("Some text");
        httpSourceTest.setDate(Instant.now().toString());

        ElasticConnection connection = ElasticConnection.getConnection(ES_TEST_HOST, ES_REST_TEST_PORT, ES_REST_TEST_SCHEME);
        EsHttpSourceTestOperations esHttpSourceTestOperations = EsHttpSourceTestOperations.getInstance(connection, INDEX_ALIAS, DOC_TYPE);

        assertEquals(0, esHttpSourceTestOperations.all().size());

        esHttpSourceTestOperations.save(httpSourceTest);
        Thread.sleep(5500);

        List<HttpSourceTest> tests = esHttpSourceTestOperations.all();
        assertEquals(1, tests.size());
        HttpSourceTest fromEs = tests.get(0);
        assertEquals(httpSourceTest.getUrl(), fromEs.getUrl());
        assertEquals(httpSourceTest.getSource(), fromEs.getSource());
        assertEquals(httpSourceTest.getHtml(), fromEs.getHtml());
        assertEquals(httpSourceTest.getUrlAccepted(), fromEs.getUrlAccepted());
        assertEquals(httpSourceTest.getTitle(), fromEs.getTitle());
        assertEquals(httpSourceTest.getText(), fromEs.getText());
        assertEquals(httpSourceTest.getDate(), fromEs.getDate());
    }

    @Test
    @Ignore
    public void testSearch() {
        ElasticConnection connection = ElasticConnection.getConnection("localhost", 9200, "http");
        EsHttpSourceTestOperations ops = EsHttpSourceTestOperations.getInstance(connection, "cf-http_source_tests_v1", "http_source_test");
        System.out.println(">>" + ops.all());

        PageableList<HttpSourceTest> pageableList = ops.filter("re", 0);
        System.out.println(pageableList.getTotalCount());
        System.out.println(pageableList.getItems().get(0));

    }

}
