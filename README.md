# Starfish

[![Travis (.com)](https://img.shields.io/travis/com/DEX-Company/starfish-java.svg)](https://travis-ci.com/DEX-Company/starfish-java)
[![GitHub contributors](https://img.shields.io/github/contributors/DEX-Company/starfish-java.svg)](https://github.com/DEX-Company/starfish-java/graphs/contributors)

Starfish is an open-sourced developer toolkit for the data economy.


## About

Starfish is an open-sourced developer toolkit for the data economy. Available in flavours of Java, Python, Clojure and JavaScript*, it allows developers, data scientists and enterprises to create, interact, integrate and manage a data supply line through standardised and simple-to-use APIs.

Based on an underlying data ecosystem standard, Starfish provides high-level APIs for common tasks within the data economy, for example, registering/publishing an asset, for subsequent use in a data supply line. In this case, an asset can be any data set, model or data service. The high-level API also allows developers to invoke operation on an asset, e.g. computing a predictive model or anonymising sensitive personal information, among other capabilities. 

Starfish works with blockchain networks, such as Ocean Protocol, and common web services through agents, allowing unprecedented flexibility in asset discovery and data supply line management.

While we strive to deliver code at a high quality, please note, that there exist parts of the library that still need thorough testing.
Contributions -- whether it is in the form of new features, better documentation or tests -- are welcome.

## Build Instructions

For simply using Starfish, you may use the Maven artifacts which are available in the [Maven Central repository][maven-central].
Squid-java requires Java 10 and Maven >= 3.5.2

#### Building development versions

If you intend to use development versions of Starfish, you can either use the deployed SNAPSHOT artifacts from the continuous integration server (see [Using Development Versions](https://github.com/DEX-Company/starfish-java)), or build them yourself.
Simply clone the development branch of the repository

```
git clone -b develop https://github.com/DEX-Company/starfish-java
```

and run a single `mvn clean install`.
This will build all the required maven artifacts and will install them in your local Maven repository, so that you can reference them in other projects.


#### Installation

Typically in Maven you can add squid-java as a dependency:

```xml
<!-- https://mvnrepository.com/artifact/sg.dex/starfish-java -->
<dependency>
    <groupId>sg.dex</groupId>
    <artifactId>starfish-java</artifactId>
    <version>0.6.0</version>
</dependency>

```

Starfish-java requires Java 8 and Maven >= 3.0

## Documentation

All the API documentation is hosted of javadoc.io:

- **[http://shrimp.octet.services/starfish/starfish-java/latest/apidocs/](http://shrimp.octet.services/starfish/starfish-java/latest/apidocs/)**


## Testing

You can run both, the unit and integration tests by using:

```bash
mvn clean install -P all-tests
```

### Unit Tests

You can execute the unit tests only using the following command:

```bash
mvn clean install
```

### Integration Tests

You can execute the integration tests using the following command:

```bash
mvn clean install -P integration-test
```

### All the tests

You can run the unit and integration tests running:

```bash
mvn clean install -P all-tests
```

## Mailing Lists

  * [developer@dex.sg][starfish-qa] -- General questions regarding the usage of Starfish.


## Maintainers

 [Developer Dex team][developer@dex.sg]

## License

Copyright 2018 Ocean Protocol Foundation Ltd.
Copyright 2018-2019 DEX Pte. Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[1]: http://www.apache.org/licenses/LICENSE-2.0
[2]: https://www.dex.sg/
[3]: https://github.com/DEX-Company/starfish-java
[4]: https://travis-ci.com/DEX-Company/starfish-java

