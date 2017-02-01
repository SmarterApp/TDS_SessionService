#!/bin/sh
#-----------------------------------------------------------------------------------------------------------------------
# File:  docker-startup.sh
#
# Desc:  Start the tds-session-service.jar with the appropriate properties.
#
#-----------------------------------------------------------------------------------------------------------------------

java \
    -Dspring.datasource.url="jdbc:mysql://${SESSION_DB_HOST}:${SESSION_DB_PORT}/${SESSION_DB_NAME}" \
    -Dspring.datasource.username="${SESSION_DB_USER}" \
    -Dspring.datasource.password="${SESSION_DB_PASSWORD}" \
    -Dspring.datasource.type=com.zaxxer.hikari.HikariDataSource \
    -Dspring.datasource.driver-class-name=com.mysql.jdbc.Driver \
    -jar /tds-session-service.jar \
    --server-port="8080" \
    --server.undertow.buffer-size=16384 \
    --server.undertow.buffers-per-region=20 \
    --server.undertow.io-threads=64 \
    --server.undertow.worker-threads=512 \
    --server.undertow.direct-buffers=true \
