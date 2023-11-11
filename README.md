Java Security
============
This repository contains several Java web applications and command line applications covering different security topics. Have a look at my [slides](https://blog.dominikschadow.de/events) and [publications](https://blog.dominikschadow.de/publications) covering most applications in this repository.

# Requirements
- [Java 21](https://dev.java)
- [Maven 3](http://maven.apache.org)
- [Mozilla Firefox](https://www.mozilla.org) (recommended, some demos might not be fully working in other browsers)
- [Docker](https://www.docker.com) (required for running the sample applications as Docker containers) 

# Web Applications in Detail
Some web applications contain exercises, some are only there to inspect and learn. Instructions are provided in detail on the start page of each web application.

Some web applications are based on [Spring Boot](http://projects.spring.io/spring-boot) and can be started via the **main** method in the **Application** class or via **mvn spring-boot:run** in the project directory. Spring Boot projects can be launched via `docker run -p 8080:8080 dschadow/[PROJECT]` after the image has been created using `mvn spring-boot:build-image`. The other web applications either contain an embedded **Tomcat7 Maven plugin** which can be started via **mvn tomcat7:run-war**, or an embedded **Jetty Maven plugin** which can be started via **mvn jetty:run-war**.

## access-control-spring-security
Access control demo project utilizing [Spring Security](http://projects.spring.io/spring-security) in a Spring Boot application. Shows how to safely load user data from a database without using potentially faked frontend values. After launching, open the web application in your browser at **http://localhost:8080**.

## csp-spring-security
Spring Boot based web application using a Content Security Policy (CSP) header. After launching, open the web application in your browser at **http://localhost:8080**.

## csrf-spring-security
Cross-Site Request Forgery (CSRF) demo project based on Spring Boot preventing CSRF in a web application by utilizing [Spring Security](http://projects.spring.io/spring-security). After launching, open the web application in your browser at **http://localhost:8080**. 

## csrf
Cross-Site Request Forgery (CSRF) demo project preventing CSRF in a JavaServer Pages (JSP) web application by utilizing  the [Enterprise Security API (ESAPI)](https://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API). After launching, open the web application in your browser at **http://localhost:8080/csrf**.

## direct-object-references
Direct object references (and indirect object references) demo project using Spring Boot and utilizing the [Enterprise Security API (ESAPI)](https://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API). After launching, open the web application in your browser at **http://localhost:8080**.

## intercept-me
Spring Boot based web application to experiment with [OWASP ZAP](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project) as intercepting proxy. Target is to receive **SUCCESS** from the backend. After launching, open the web application in your browser at **http://localhost:8080**.

## security-header
Security response header demo project which applies **X-Content-Type-Options**, **Cache-Control**, **X-Frame-Options**, **HTTP Strict Transport Security (HSTS)**, **X-XSS-Protection** and **Content Security Policy (CSP)** (Level 1 and 2) headers to HTTP responses. After launching, open the web application in your browser at **http://localhost:8080/security-header** or **https://localhost:8443/security-header**.

## security-logging
Spring Boot based web application utilizing the [OWASP Security Logging Project](https://www.owasp.org/index.php/OWASP_Security_Logging_Project). Demonstrates how to log security relevant incidents in a log file. After launching, open the web application in your browser at **http://localhost:8080**.

## session-handling-spring-security
Session handling demo project based on Spring Boot utilizing [Spring Security](http://projects.spring.io/spring-security) and [jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot) to secure [Spring](http://spring.io) configuration (property) files. Shows how to restrict access to resources (URLs), how to apply method level security and how to securely store and verify passwords. Uses Spring Security for all security related functionality. Requires a system property (or environment variable or command line argument) named **jasypt.encryptor.password** with the value **session-handling-spring-security** present on startup. After launching, open the web application in your browser at **http://localhost:8080**.

## session-handling
Session handling demo project using plain Java. Uses plain Java to create and update the session id after logging in. Requires a web server with Servlet 3.1 support. After launching, open the web application in your browser at **http://localhost:8080/session-handling**.

## sql-injection
Spring Boot based web application to experiment with normal (vulnerable) statements, statements with escaped input, and prepared statements. After launching, open the web application in your browser at **http://localhost:8080**.

## xss
Cross-Site Scripting (XSS) demo project preventing XSS in a JavaServer Pages (JSP) web application by utilizing input validation, output escaping with [OWASP Java Encoder](https://www.owasp.org/index.php/OWASP_Java_Encoder_Project) and the Content Security Policy (CSP). After launching, open the web application in your browser at **http://localhost:8080/xss**.

# Command Line Applications in Detail
The following projects demonstrate crypto usage in Java with different libraries. Each project contains one or more JUnit **test** classes to test various functionalities of the demo project.

## crypto-hash
Crypto demo using Java to hash passwords with different hashing algorithms.

## crypto-java
Crypto demo using plain Java to encrypt and decrypt data with asymmetric (RSA) and symmetric (AES) algorithms as well as to sign and verify data (DSA).

## crypto-shiro
Crypto demo using [Apache Shiro](http://shiro.apache.org) to encrypt and decrypt data with symmetric (AES) algorithms as well as hash data (passwords). 

## crypto-tink
Crypto demo using [Google Tink](https://github.com/google/tink) to encrypt and decrypt data with asymmetric and hybrid encryption, MAC and digital signatures. Depending on the demo, keys are either generated on the fly or stored/loaded from the keysets' directory. The **AWS KMS** samples (classes with AwsKms in their names) require a configured AWS KMS with an enabled master key.

## Meta
![Build](https://github.com/dschadow/JavaSecurity/workflows/Build/badge.svg) [![codecov](https://codecov.io/gh/dschadow/JavaSecurity/branch/main/graph/badge.svg?token=3raAUutQ8l)](https://codecov.io/gh/dschadow/JavaSecurity) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)