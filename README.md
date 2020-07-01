# Melinoe
![Java CI with Maven](https://github.com/Goodie01/Melinoe/workflows/Java%20CI%20with%20Maven/badge.svg)
![Sonar quality gate](https://img.shields.io/sonar/quality_gate/Goodie01_BookInformation?server=https://sonarcloud.io)


The goal is to provide a testing framework that can cover testing all aspects of a application in a similar method, that can have new components added to it as needed. Eg Testing a thick client GUI application.

Melinoë is a chthonic nymph or goddess invoked in one of the Orphic Hymns and represented as a bringer of nightmares and madness. This seems particularly suitable for a testing framework.

Hecate is most often shown holding a pair of torches or a key, perfect for a component that shines a light on outgoing, often invisible, requests.

## Goals
1. Test a web application
1. Test a REST api
1. Test that when you perform a action via a rest API or UI, a outgoing request is made
1. Detailed logs of all success and failures can be seen after a run

## Melinoe is being created in at multiphased approach phases
1. Create framework
   1. Selenium based web testing
   1. Testing a rest API
1. Create Hecate, a component to test outgoing API requests
   1. The target application will talk to this in place of external services, and our tests can modify the configuration of, on the fly
   1. Eg set specific responses, artificially inflate the response time, time out altogether
   1. Will store logs for validating requests were made
1. Create Hecate-UI, which will allow testers to be able view logs and configure the new component on the fly

## Componets
1. Test library, intended as a java library to be pulled in instead of JUnit, to write intergration tests
1. Outgoing Rest API proxy (This needs a succinct name)
1. Outgoing REST API proxy UI


## Quality
* [Framework Sonar](https://sonarcloud.io/dashboard?id=Goodie01_BookInformation)
* [Examples Sonar](https://sonarcloud.io/dashboard?id=Goodie01_BookInformation2)