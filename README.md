# Spring Boot Custom Security

This project was created as proof of concept for using [Spring Boot][1] and [Spring Security][2] to build an secure a
REST application.  The application assumes that some security proxy, like [Open Repose][3], sits in front and intercepts
all requests to handle AuthN and possibly some form of AuthZ.

## Running the App
This app requires Java 1.8 or higher.  To run the app use `./gradlew bootRun`.  More information can be 
found on the [Sprint Boot][1] website.

[1]: http://projects.spring.io/spring-boot/
[2]: http://projects.spring.io/spring-security
[3]: http://www.openrepose.org/