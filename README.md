Build Metadata Rest Service
======================================

Build Meta holds all the component builds' metadata for implementing such as contintous automatic deployment system.

Context and Aims
======================================

When I was implementing a platform service, always required to build rest services, but can not get on-the-shelf examples that can be used for product service, most of the examples do not give the enough implementations
that meeting the production level security requirements etc, most only provides simple get started usage. 
At the same time, lots of people ask the same question in Stackflow and other sites, "is there such on-the-shelf Rest Service examples that support 2-legged OAuth?"

Now, the answer is Yes.

The example is here, this will make people do not have to spend too much time on finding such example, work, 
and provide them a good start point for building Rest Service with production security requirements implemented using 2-legged oauth.


To Do List
======================================
1. Make an able-to-use-for-production base rest project for developing Rest Service basing on Spring and Resteasy; (Done)
2. Make Rest Service support OAuth 2 legged base on oauth 1.0; (Done)
3. Make Rest Service support Https; (To Do)
4. Make curl example to call Rest APIs; (Done)
5. Make unit test example that do not start web server then can test Rest API, uses MockDispatcherFactory; (Done)
6. Practice Restful api design, design api for each cases(CRUD + actions) (Done)
7. Make Rest Cient SDK and get a method that design Rest Client SDK. (Done)
learnt: UI models with Rest Service, UI models with End App that calling SDK,
in Client SDK, it should have self domain models, this domain models will be 
used by Client SDK and Client SDK's calling app.
8. Make Rest Docs and make a framework for making rest docs for future use. (To Do)
9. Make have easy to use Rest API exception handling that using Resteasy ExceptionHandler 
to return error message json when there is exception while normally return normal 
response json. need to use @Component to make ExceptionHandler load by spring-resteasy. (Done)
10. With easy to use Validation Framework  (To Do)
11. Make unit test example that build base on spring test framework (Done)
12. Make bootstrap Admin UI example. (To Do)
13. Make Cobertura Test Coverage usage example  (To Do)
14. Make unit tests examples for CRUD APIs, in setup and teardown create and delete, in createTest Case
verify if is created. (Done)


Developer Guide
======================================
1. Deployment

System Requirements:
-----------------------------------------
- Maven 2.0.9 or higher

Added client module. The module shows an example to test the existing 
war generated by the service module using the same
web container: jetty6 in an embedded mode. The container is injected 
in the junit 4 test. The client makes use of the resteasy client framework.

1) Configuration and override Configurations
--------------------------------------------------------------------

`default configuration:  classpath*:/application.properties`

`override Configurations: file:/var/flysnow/buildmeta/application.properties`

can configure db properties in application.properties

2) Create database and tables

`cd $BUILD_META_PROJECT_HOME`

`mysql -ubuildmeta -pbuildmeta buildmeta < src/main/resources/dbdeploy/buildmeta_2014-09-09.sql`


1. Run
-----------------

`cd $HOME`

`mvn jetty:run`

2. Run Unit Test
-----------------

1) Run all unit tests

`mvn test -Dtest=BuildRestServiceUnitTest`

2) Run a test case specified

`mvn test -Dtest=BuildRestServiceSmokeTest#testCreateBuildInfoAPI`

`mvn test -Dtest=BuildRestServiceSmokeTest#testGetBuildsByParams`

3. Run SmokeTest
-----------------

1) Run BuildMetadata Service

  `mvn jetty:run` 
  
2) Run Smoke Test

  `mvn test -Dtest=BuildRestServiceSmokeTest`

  `mvn test -Dtest=BuildMetadataServiceSmokeTest`

4. Call Rest APIs by curl
----------------------------------

1) Set logger logging level

`curl -v -X GET -H "Content-Type: application/json" http://localhost:8080/ws/logging/org.flysnow/DEBUG`

2)

5. Debug Tools
-----------------

1) change logging level

`curl -v -X GET -H "Content-Type: application/json" http://localhost:8080/ws/logging/org.flysnow/DEBUG`

6. Disable API OAuth
---------------------------------------------------

in file src/main/webapp/WEB-INF/applicationContext.xml comment /ws/builds url pattern:

`<!--  <sec:intercept-url pattern="/ws/builds/**" access="ROLE_CONSUMER" /> -->`


Python OAuth Client:
=============================

import OAuth2LeggedClient
import requests

class BuildMetaWSOauthClient(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
        self.apiUrl="http://localhost:8080/ws/builds/data"
        self.oauthKey="devops"
        self.oauthSecret="devops2014"
        pass
    
    def getBuilds(self):
        import json

        oauth_publishapi = OAuth2LeggedClient.generateOAuthRequestUrl(base_url = self.apiUrl, 
                                                                      comsumer_key = self.oauthKey, 
                                                                      comsumer_secret = self.oauthSecret, 
                                                                      http_method = "GET")
        
        try:
            headers = {'content-type': 'application/json'}
            r = requests.get(oauth_publishapi, headers={"content-type":"application/json"}, timeout=30)
            print r.headers['content-type']
            #r = requests.post(oauth_publishapi,headers=request_header, timeout=30)
            logger.info(str(r.status_code))
            logger.info(r.text)
        except Exception, e:
            logger.error(str(e))
        pass


`client = BuildMetaWSOauthClient()`
`client.getBuilds()`
