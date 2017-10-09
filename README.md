#Session Service
## Overview
The `TDS_Session` (aka Session Support Service) consists of two modules:

* **client:** Contains the POJOs/classes needed for a consumer to interact with the Session Support Service
* **service:** REST endpoints that provide TDS session data


## Setup
There are a few things you will need to do to build the project.  The following tools will be needed to build the project.

1. Maven
2. Java 7 - for the client project
3. Java 8 - for the server project

In addition to the tools the project also uses [Maven toolchains](https://maven.apache.org/guides/mini/guide-using-toolchains.html) since the client needs to be built at Java 1.7 to support legacy applications.  You will need to either append to your current toolchain file or create a new one in your local .m2 directory.

**Sample Mac OS Version**  

```<?xml version="1.0" encoding="UTF8"?>
<toolchains>
  <!-- JDK toolchains -->
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>1.7</version>
    </provides>
    <configuration>
      <jdkHome>/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home</jdkHome>
    </configuration>
  </toolchain>
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>1.8</version>
    </provides>
    <configuration>
      <jdkHome>/Library/Java/JavaVirtualMachines/jdk1.8.0_102.jdk/Contents/Home</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
```

## Build
To build the **client** and **service**, use the "parent" `pom.xml` that is contained in the `TDS_SessionService` directory:

* `mvn clean install -f /path/to/parent/pom.xml`

To build the **client**:

* `mvn clean install -f /path/to/client/pom.xml`

To build the **service**:

* `mvn clean install -f /path/to/service/pom.xml`

To build the service and run integration tests:
  
* `mvn clean install -Dintegration-tests.skip=false -f /path/to/service/pom.xml`

# Additional Documentation
Additional Documentation can be found in the documentation folder.  This includes:

* [Spring Configuration](documentation/spring_configuration.md)
* [Running Session Microservice](running_service.md)

