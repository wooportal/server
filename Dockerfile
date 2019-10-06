FROM alpine:latest
LABEL maintainer info@codeschluss.de
ADD / /tmp/wooportal.server
RUN \
#
# packages
apk --no-cache add \
  nginx \
  openjdk8-jre && \
apk --no-cache --virtual build add \
  maven \
  openjdk8 && \
#
# wooportal/server
cd /tmp/wooportal.server && \
mvn -B install -DskipTests=true \
  -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn && \
mkdir -p /opt/wooportal.server && \
mv /tmp/wooportal.server/target/*.jar /opt/wooportal.server/server.jar && \
#
# cleanup
apk del --purge build && \
find /root /tmp -mindepth 1 -delete
#
# runtime
EXPOSE 8080
WORKDIR /opt/wooportal.server
CMD \
  nginx -c /conf/nginx.conf && \
  java -jar server.jar
