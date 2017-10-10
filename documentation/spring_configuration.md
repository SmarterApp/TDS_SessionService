# Spring configuration YAML explanation
Below is a sample Spring boot yaml configuration file for the Session microservice.

If you are using Spring Cloud you can leverage [Spring CLI](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-installing-spring-boot.html#getting-started-installing-the-cli) to secure your passwords.

```
# Contains properties for the tds-session-service.
# Properties not defined in a profile are global to all profiles.  Profiles sections can override the global values.
# Contains properties for the tds-assessment-service.
# Properties not defined in a profile are global to all profiles.  Profiles sections can override the globabl values.

# Undertow Web Server configuration
server:
  port: ${SERVER_PORT:8080}
  undertow:
    buffer-size: 8192
    max-regions: 10
    io-threads: 4
    worker-threads: 32
    direct-buffers: true

# General Spring Datasource information that doesn't need to change
spring:
  datasource:
    hikari:
      maximum-pool-size: 32
      minimum-idle: 8
      idle-timeout: 120000
      connectionTestQuery: "SELECT 1"

# Flyway should not be run by default in the session service
flyway:
  enabled: false

# Health checks.  Disable redis and rabbit health checks.
management:
  health:
    redis:
      enabled: false
    rabbit:
      enabled: false

---
# The three dashes indicates that this is a new configuration inheritting from the defaults
spring:
  profiles: # Replace with the Spring profile referenced in the kubernetes deployments (or docker-compose)
  datasource:
    url: jdbc:mysql://<DB SERVER>/session
    username: # username for the DB
    password: # password for the DB
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: 'com.mysql.jdbc.Driver'
  redis: 
    sentinel: # this is configured if running redis within kubernetes or local
      master: # master
      nodes: # nodes for redis
    host: # this should point to the redis server if deployed outside of kubernetes
  rabbitmq:
    addresses: # rabbit mq addresses
    username: # username for rabbitmq
    password: # password for rabbitmq

# Configure the cache implementation.  Right now 'redis' is supported.  
# Leaving this out will result in the in-memory cache being used
tds:
  cache:
    implementation: redis

# Session service needs to make calls to the exam service so the examUrl needs
# to point to whichever exam service is running.  The example below is if 
# you are leveraging our kubernetes deployment
session-service:
  examUrl: http://tds-exam-service/

logstash-destination: # Logstash location if configured

```