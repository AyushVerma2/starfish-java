# Starfish-java

[![Travis (.com)](https://img.shields.io/travis/com/DEX-Company/starfish-java.svg)](https://travis-ci.com/DEX-Company/starfish-java)
[![GitHub contributors](https://img.shields.io/github/contributors/DEX-Company/starfish-java.svg)](https://github.com/DEX-Company/starfish-java/graphs/contributors)
[![Maven Central](https://img.shields.io/maven-central/v/sg.dex/starfish-java)](https://search.maven.org/artifact/sg.dex/starfish-java/)

>Starfish is an open-sourced developer toolkit for the data economy. The toolkit allows developers, data scientists and enterprises to create, integrate and manage data supply lines through standardised and simple-to-use APIs. 

[Developer site](https://developer.dex.sg/)

---

## Table of Contents

* [Features](#features)
* [Installation](#installation)
* [Configuration](#configuration)
  * [Using Starfish-Java with Surfer](#using-starfish-java-with-surfer)
* [Documentation](#documentation)
* [Testing](#testing)
  * [Unit Tests](#unit-tests)
  * [Integration Tests](#integration-tests)
* [Maintainers](#maintainers)
* [License](#license)

---

## Features

Starfish is a open source client library for the decentralised data economy, based on underlying open data ecosystem standards (see [Data Ecosystem Proposals](https://https://github.com/DEX-Company/DEPs/))

Starfish provides high-level APIs for common tasks within the data economy, for example registering/publishing a data asset for subsequent use in a digital supply line. In this case, an asset can be any data set, model or data service.

Starfish is designed to be able to work with blockchain networks, such as Ocean Protocol, and common web services through agents, allowing unprecedented flexibility in asset discovery and digital supply line management.

### Effective Way to Build and Manage Digital Supply Lines
Starfish provides a common abstraction to enable decentralised data infrastructure to interoperate effectively, allowing data supply lines to be easily created and managed using standardised interface. 

### Data Sharing Made Simple for Everyone
Any existing data resources can be "packaged" into a Data Asset, allowing data exchange possible with any data ecosystem participants. 

### Empowers Innovative Application in the Space of Data
There is no practical limit to the types of operations that can be created, and potentially recombined in interesting ways to create novel data solutions. Orchestration of such operations with Starfish is a perfect way to facilitate rapid innovation in data and AI solutions, especially where these solutions must orchestrate data and services.


---
## Installation

Typically in Maven you can add starfish-java as a dependency:

```xml
<!-- https://mvnrepository.com/artifact/sg.dex/starfish-java -->
<dependency>
    <groupId>sg.dex</groupId>
    <artifactId>starfish-java</artifactId>
    <version>0.8.0</version>
</dependency>

```

Starfish-java requires Java 11 and Maven >= 3.0

## Configuration

You can configure the library using a Java Properties Object

### Using Starfish-Java with Surfer

If you are using [Surfer](https://github.com/DEX-Company/surfer/) for playing with the Starfish , you can refer the developer testcase (https://github.com/DEX-Company/starfish-java/tree/develop/src/integration-test/java/developerTC)

## Documentation

All the API documentation is hosted of javadoc.io:
- **[https://dex-company.github.io/starfish-java/docs](https://dex-company.github.io/starfish-java/apidocs/)**

## Code Coverage
Code Coverage:
- **[https://dex-company.github.io/starfish-java/coverage](https://dex-company.github.io/starfish-java/jacoco/)**

## Testing

You can run both, the unit and integration tests by using:

```bash
mvn clean install -P all-tests
```

### Unit Tests

You can execute the unit tests only using the following command:

```bash
mvn clean install
mvn test
```

### Integration Tests

You can execute the integration tests using the following command:

```bash
mvn clean verify -P integration-test
```

### All the tests

You can run the unit and integration tests running:

```bash
mvn clean install -P all-tests
```

## Maintainers

 - [DEX Developer team](developer@dex.sg)

## License

Copyright 2018 Ocean Protocol Foundation Ltd.
Copyright 2018-2020 DEX Pte. Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


