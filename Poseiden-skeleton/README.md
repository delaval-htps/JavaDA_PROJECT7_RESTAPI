# spring-boot-skeleton

## Running the application locally
There are several ways to run application on your local machine. One way is to execute the main method in the com.nnk.springboot.Application.java from your IDE.

Alternatively you can use the Spring Boot Maven plugin like so:

`mvn spring-boot:run`

## Technical:
This application was upgraded 'cause of some jars for examle for hibernate's validation not compatibles. Now this application uses:
1. Framework: Spring Boot v2.7.4
2. Java 8
3. Thymeleaf
4. Bootstrap v.4.3.1
5. Junit v.5.9.1
6. Jacoco v.0.8.7

## Run test
We used TDD to implement code in this project:

So, from creation of integration and unit tests, we created the source's code and check it to be sure of its correct working.

* To run unit tests only, you can use command: 
    ```shell
    mvn spring-boot:run
    ```

* To run integration tests only, you can use command:
    ```shell
   mvn failsafe:integration-test
    ```
* If you want to launch a build phase without integration tests, you can use next option for example:  
  ```shell
    mvn verify -Dskip.it=true
    ```
## Jacoco Coverage
A report of coverage is automatically done when you launch tests.

you can access to it at location : `target/site/jacoco/index.html`


## Versions
* V.0.1.0 :
    * Fork and Clone project in locale
    * Upgrade application to resolve problem of dependencie's jar
    * Implementation of all models with constructors to resolve problems of errors
    * Implementation of service, controller and view for BidList using TDD
* V.0.2.0:
    * Implementation of all services,controllers and views for all entities using TDD
    * Add Jacoco plugin to have report for tests
    * Clean code and update pom.xml

## Todo: Security
1. Create user service to load user from  database and place in package com.nnk.springboot.services
2. Add configuration class and place in package com.nnk.springboot.config
