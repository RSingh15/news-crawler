STORM_HOME=/opt/storm/apache-storm-1.1.1
mvn clean install -Pbigjar -Dstorm.scope=provided -DskipTests
$STORM_HOME/bin/storm jar crawler/target/crawler-standalone.jar CrawlerTopology -conf crawler/conf/local.yaml
