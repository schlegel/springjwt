# REST with Spring Boot, JPA and JWT Security

## Development Information

> Eclipse Development: To activate code generation for the mapstruct library, go to global Preferences/Maven/Annotation Processing and choose "Experimental: Delegate Annotation Processing to maven plugins (for maven-processor-plugin only)

## Spring Boot Application

You can run a Spring Boot application with embedded tomcat or provided tomcat.

1. Run Application.class with Maven profile inMemory - Includes an embedded Tomcat
2. Deploy the war file with Maven profile inMemory-provided-tomcat (mvn package -P inMemory-provided-tomcat) - Excludes an embedded Tomcat

There exists also a prepared mysql profile which allows the connection to a provided mysql database instead of the in memory database.


## REST Endpoints:

POST /authentication :
   
    - Param: String email ("user@test.de"), String password ("password")
    - Returns : JWT Token

GET /security :

    - HEADER: "Authorization" with JWT token string (e.g.: Authorization: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3QuZGUiLCJuYmYiOjE0NTI2MzIwNDIsImV4cCI6MTQ1MzQxNzIwMiwiaWF0IjoxNDUyNjMyMDQyfQ.2lBZ42kfHqsSZVoFEdeN76OKXhhA3tBnecYiOliEjtk)
    - Returns: Security roles of the user
    
GET /ping :
    
    - Returns: "Hello World"
    
GET /securedping :
   
    - HEADER: "Authorization" with JWT token string (e.g.: Authorization: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHRlc3QuZGUiLCJuYmYiOjE0NTI2MzIwNDIsImV4cCI6MTQ1MzQxNzIwMiwiaWF0IjoxNDUyNjMyMDQyfQ.2lBZ42kfHqsSZVoFEdeN76OKXhhA3tBnecYiOliEjtk)
    - Returns: "Hello Secured World" IF AUTH TOKEN VALID
   
## References

- [Spring Boot Documentation](http://docs.spring.io/spring-boot/docs/1.3.1.RELEASE/reference/htmlsingle/)
- [Spring Boot Maven Plugin Manual](http://docs.spring.io/spring-boot/docs/1.3.1.RELEASE/maven-plugin/)