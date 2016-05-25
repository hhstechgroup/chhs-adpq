#!/bin/sh
################################################################################
# PostgreSQL
################################################################################
if [ -d ${SPRING_DATASOURCE_URL} ]; then
  SPRING_DATASOURCE_URL="jdbc:postgresql://${POSTGRESQL_PORT_5432_TCP_ADDR}:${POSTGRESQL_PORT_5432_TCP_PORT}/chhs-apqd"
  echo "SPRING_DATASOURCE_URL autoconfigured by docker link: ${SPRING_DATASOURCE_URL}"
else
  echo "SPRING_DATASOURCE_URL init by configuration: ${SPRING_DATASOURCE_URL}"
fi

################################################################################
# ElasticSearch
################################################################################
if [ -d ${SPRING_DATA_ELASTICSEARCH_CLUSTER_NODES} ]; then
  SPRING_DATA_ELASTICSEARCH_CLUSTER_NODES="${ELASTIC_PORT_9300_TCP_ADDR}:${ELASTIC_PORT_9300_TCP_PORT}"
  echo "SPRING_DATA_ELASTICSEARCH_CLUSTER_NODES autoconfigured by docker link: ${SPRING_DATA_ELASTICSEARCH_CLUSTER_NODES}"
else
  echo "SPRING_DATA_ELASTICSEARCH_CLUSTER_NODES init by configuration: ${SPRING_DATA_ELASTICSEARCH_CLUSTER_NODES}"
fi
if [ -d ${JHIPSTER_SLEEP} ]; then
  JHIPSTER_SLEEP=20
fi
################################################################################
# Neo4j
################################################################################
if [ -d ${NEO4J_HTTP_URI} ]; then
  NEO4J_HTTP_URI="http://${NEO4J_PORT_7474_TCP_ADDR}:${NEO4J_PORT_7474_TCP_PORT}"
  echo "NEO4J_HTTP_URI autoconfigured by docker link: ${NEO4J_HTTP_URI}"
else
  echo "NEO4J_HTTP_URI init by configuration: ${NEO4J_HTTP_URI}"
fi

if [ -d ${JHIPSTER_SLEEP} ]; then
  JHIPSTER_SLEEP=20
fi

################################################################################
# Start application
################################################################################
echo "The application will start in ${JHIPSTER_SLEEP}sec..." && sleep ${JHIPSTER_SLEEP}
if [ -d ${JHIPSTER_SPRING} ]; then
  java -jar /chhs-apqd.war \
    --spring.profiles.active=prod ${JHIPSTER_SPRING_ADD} \
    --spring.data.elasticsearch.cluster-nodes=${SPRING_DATA_ELASTICSEARCH_CLUSTER_NODES} \
    --spring.datasource.url=${SPRING_DATASOURCE_URL} \
    --neo4j.http.uri=${NEO4J_HTTP_URI}
else
  echo "java -jar /intake.war --spring.profiles.active=prod ${JHIPSTER_SPRING}"
  java -jar /chhs-apqd.war --spring.profiles.active=prod ${JHIPSTER_SPRING}
fi
