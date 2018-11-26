package org.ninestar.crawling.crawler;

import org.ninestar.crawling.es.EsDocumentOperations;
import org.ninestar.crawling.es.EsHttpSourceOperations;
import org.ninestar.crawling.es.EsHttpUrlOperations;

import java.util.Map;

/***
 * Interface for external service factory.
 */
public interface ServiceProvider {

    EsHttpUrlOperations createEsHttpUrlOperations(Map conf);

    EsHttpSourceOperations createEsHttpSourceOperations(Map conf);

    EsDocumentOperations creatEsDocumentOperations(Map conf);
}
