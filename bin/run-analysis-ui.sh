( cd analysis-ui && mvn clean package -Pbigjar && java -Dconfig=conf/development.properties -jar target/analysis-ui-standalone.jar )