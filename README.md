Java Security
============
This repository contains several Java web applications and command line applications covering different security topics. Have a look at my [slides](https://blog.dominikschadow.de/events) and [publications](https://blog.dominikschadow.de/publications) covering most applications in this repository.

# Requirements
- [Java 25](https://dev.java)
- [Maven 3.6.3+](https://maven.apache.org)
- [Mozilla Firefox](https://www.mozilla.org) (recommended, some demos might not be fully working in other browsers)
- [Docker](https://www.docker.com) (required for running the sample applications as Docker containers) 

# Web Applications in Detail
Some web applications contain exercises, some are only there to inspect and learn. Instructions are provided in detail on the start page of each web application.

Some web applications are based on [Spring Boot](https://spring.io/projects/spring-boot) and can be started via the **main** method in the **Application** class or via **mvn spring-boot:run** in the project directory. Spring Boot projects can be launched via `docker run -p 8080:8080 dschadow/[PROJECT]:[VERSION]` after the image has been created using `mvn spring-boot:build-image`. The other web applications are plain Jakarta Servlet applications which run on [Apache Tomcat 11](https://tomcat.apache.org) via the [Cargo Maven plugin](https://codehaus-cargo.github.io/cargo/Maven+3+Plugin.html) and can be started via **mvn package cargo:run** in the project directory.

## access-control-spring-security
Access control demo project using [Spring Security](https://spring.io/projects/spring-security) in a Spring Boot application. Shows how to safely load user data from a database without using potentially faked frontend values. After launching, open the web application in your browser at **http://localhost:8080**.

## csp-spring-security
Spring Boot based web application using a Content Security Policy (CSP) header. After launching, open the web application in your browser at **http://localhost:8080**.

## csrf-spring-security
Cross-Site Request Forgery (CSRF) demo project based on Spring Boot preventing CSRF in a web application by using [Spring Security](https://spring.io/projects/spring-security). After launching, open the web application in your browser at **http://localhost:8080**. 

## csrf
Cross-Site Request Forgery (CSRF) demo project preventing CSRF in a JavaServer Pages (JSP) web application with an anti CSRF token implementation based on the [Enterprise Security API (ESAPI)](https://owasp.org/www-project-enterprise-security-api/). After launching, open the web application in your browser at **http://localhost:8080/csrf**.

## direct-object-references
Direct object references (and indirect object references) demo project using Spring Boot and using the [Enterprise Security API (ESAPI)](https://owasp.org/www-project-enterprise-security-api/). After launching, open the web application in your browser at **http://localhost:8080**.

## intercept-me
Spring Boot based web application to experiment with [ZAP](https://www.zaproxy.org) as an intercepting proxy. Target is to receive **SUCCESS** from the backend. After launching, open the web application in your browser at **http://localhost:8080**.

## security-header
Security response header demo project which applies **X-Content-Type-Options**, **Cache-Control**, **X-Frame-Options**, **HTTP Strict Transport Security (HSTS)**, **X-XSS-Protection** and **Content Security Policy (CSP)** (Level 1 and 2) headers to HTTP responses. After launching, open the web application in your browser at **http://localhost:8080/security-header** or **https://localhost:8443/security-header**.

## security-logging
Spring Boot based web application utilizing the [OWASP Security Logging Project](https://github.com/javabeanz/owasp-security-logging). Demonstrates how to log security relevant incidents with security markers. After launching, open the web application in your browser at **http://localhost:8080**.

## session-handling-spring-security
Session handling demo project based on Spring Boot utilizing [Spring Security](https://spring.io/projects/spring-security). Shows how to restrict access to resources (URLs), how to apply method level security and how to store and verify hashed passwords. Uses Spring Security for all security-related functionality. After launching, open the web application in your browser at **http://localhost:8080**.

## session-handling
Session handling demo project using plain Java. Uses plain Java to create and update the session id after logging in. After launching, open the web application in your browser at **http://localhost:8080/session-handling**.

## sql-injection
Spring Boot based web application to experiment with normal (vulnerable) statements, statements with escaped input, and prepared statements. After launching, open the web application in your browser at **http://localhost:8080**.

## xss
Cross-Site Scripting (XSS) demo project preventing XSS in a JavaServer Pages (JSP) web application by using input validation, output escaping with [OWASP Java Encoder](https://owasp.org/www-project-java-encoder/) and the Content Security Policy (CSP). After launching, open the web application in your browser at **http://localhost:8080/xss**.

# Command Line Applications in Detail
The crypto projects demonstrate crypto usage in Java with different libraries. Each project contains one or more JUnit **test** classes to test various functionalities of the demo project.

## crypto-hash
Crypto demo using Java to hash passwords with different hashing algorithms.

## crypto-java
Crypto demo using plain Java to encrypt and decrypt data with asymmetric (RSA) and symmetric (AES) algorithms as well as to sign and verify data (DSA). Includes post-quantum samples for key encapsulation (ML-KEM) and digital signatures (ML-DSA) as well as key derivation with HKDF using the Key Derivation Function API.

## crypto-shiro
Crypto demo using [Apache Shiro](https://shiro.apache.org) to encrypt and decrypt data with symmetric (AES) algorithms as well as hash data (passwords). 

## crypto-tink
Crypto demo using [Google Tink](https://github.com/tink-crypto/tink-java) to encrypt and decrypt data with asymmetric and hybrid encryption, MAC and digital signatures. Depending on the demo, keys are either generated on the fly or stored/loaded from the keysets' directory. The **AWS KMS** samples (classes with AwsKms in their names) require a configured AWS KMS with an enabled master key.

## serialize-me
Java serialization demo showing the risks of deserializing untrusted data. Run the **main** method of the **Serializer** class first to create the serialized file, then the **main** method of the **Deserializer** class to read it again.

## Meta
![Build](https://github.com/dschadow/JavaSecurity/workflows/Build/badge.svg) [![codecov](https://codecov.io/gh/dschadow/JavaSecurity/branch/main/graph/badge.svg?token=3raAUutQ8l)](https://codecov.io/gh/dschadow/JavaSecurity) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)