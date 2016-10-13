#!/bin/sh
#-----------------------------------------------------------------------------------------------------------------------
# File:  docker-startup.sh
#
# Desc:  Start the tds-session-service.jar with the appropriate properties.
#
#-----------------------------------------------------------------------------------------------------------------------

# Determine the amount of memory available to the container, for use with setting the Xms and Xmx values when starting
# the jar (in this case setting both values to 80% of free memory).
freeMem=`awk '/MemFree/ { print int($2/1024) }' /proc/meminfo`
s=$(($freeMem/10*8))
x=$(($freeMem/10*8))

java \
    -Dspring.datasource.url="jdbc:mysql://${SESSION_DB_HOST}:${SESSION_DB_PORT}/${SESSION_DB_NAME}" \
    -Dspring.datasource.username="${SESSION_DB_USER}" \
    -Dspring.datasource.password="${SESSION_DB_PASSWORD}" \
    -Dspring.datasource.type=com.zaxxer.hikari.HikariDataSource \
    -jar /tds-session-service.jar \
    --server-port="8080" \
    --server.undertow.buffer-size=16384 \
    --server.undertow.buffers-per-region=20 \
    --server.undertow.io-threads=64 \
    --server.undertow.worker-threads=512 \
    --server.undertow.direct-buffers=true \
