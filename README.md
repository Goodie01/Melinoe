# Melinoe
Melinoë is a chthonic nymph or goddess invoked in one of the Orphic Hymns and represented as a bringer of nightmares and madness. This seems particularly suitable for a testing framework.

The goal here is to provide a testing framework that can cover testing all aspects of a application in a similar method, that can have new components added to it as needed. Eg Testing a thick client GUI application.

### Goals
1. Test a web application
1. Test a REST api
1. Test that when you perform a action via a rest API or UI, a outgoing request is made
1. Detailed logs of all success and failures can be seen after a run

### Melinoe is being created in at multiphased approach phases:
1. Create framework
   1. Selenium based web testing
   1. Testing a rest API
1. Create Hecate, a component to test outgoing API requests
   1. Create a component that the target application will talk to, and our tests can modify the configuration of, on the fly
   1. Eg set specific responses, artificially inflate the response time, time out altogether
1. Create a UI to allow testers to be able view logs and configure the new component on the fly

### Componets:
1. Test library, intended as a java library to be pulled in instead of JUnit, to write intergration tests
1. Outgoing Rest API proxy (This needs a succinct name)
1. Outgoing REST API proxy UI
