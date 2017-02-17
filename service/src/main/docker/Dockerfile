#-----------------------------------------------------------------------------------------------------------------------
# File:  Dockerfile
#
# Desc:  Create a Docker container to host the Session Support Service.
#
#-----------------------------------------------------------------------------------------------------------------------
FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp

COPY docker-startup.sh docker-startup.sh
COPY tds-session-service-0.0.1-SNAPSHOT.jar tds-session-service.jar

RUN sh -c 'touch /tds-session-service.jar /docker-startup.sh'
RUN apk --no-cache add curl

ENTRYPOINT ["/docker-startup.sh"]