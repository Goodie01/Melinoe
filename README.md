# Melinoe
![Java CI with Maven](https://github.com/Goodie01/Melinoe/workflows/Java%20CI%20with%20Maven/badge.svg)


The goal is to provide a testing framework that can cover testing all aspects of a application in a similar method, that can have new components added to it as needed. This could, eventually, extend as far as testing a thick GUI Client.

Melinoë is a chthonic nymph or goddess invoked in one of the Orphic Hymns and represented as a bringer of nightmares and madness. This seems particularly suitable for a testing framework.

## Compile and run examples

Requires
* Java 21
* Maven 3.5.0+
* Create a default.properties file in /examples/src/main/resources/default.properties
  * Only firefox is currently supported
  * The exe location should be correct for your machine

```properties
BROWSER=FIREFOX
BROWSER_EXE_LOCATION=C:\\Program Files\\Firefox Nightly\\firefox.exe
```

```bash
git clone git@github.com:Goodie01/Melinoe.git
cd Melinoe
mvn clean install -f framework/pom.xml && mvn clean test -f examples/pom.xml
```

## Goals
1. Test a web application
1. Test a REST api
1. Test that when you perform a action via a rest API or UI, a outgoing request is made
1. Detailed logs of all success and failures can be seen after a run

## Melinoe is being created in at multiphased approach phases
1. Create framework
   1. Selenium based web testing
   1. Testing a rest API
1. Think through testing outgoing API requests

## Componets
1. Test library, intended as a java library to be pulled in instead of JUnit, to write intergration tests
1. Logs reading UI
1. Outgoing Rest API proxy (This needs a succinct name)
