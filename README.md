Java Security
============
This repository contains several Java web applications and command line applications covering different security topics. Have a look at the [slides](https://blog.dominikschadow.de/events) from various events covering the applications in this repository. The **[Java Web Security Workshop](https://blog.dominikschadow.de/workshop)** uses all these applications in much greater detail.

#Web Applications in Detail
Some web applications contain exercises. Instructions are provided in detail in each web application.

Using [Mozilla Firefox](https://www.mozilla.org) as browser is strongly recommended. Some web applications are based on Spring Boot and can be started via the **main** method in the **Application** class or via **mvn spring-boot:run** . The other web applications either contain an embedded **Tomcat7 Maven plugin** which can be started via **mvn tomcat7:run-war** or an embedded **Jetty Maven plugin** which can be started via **mvn jetty:run-war**.

##access-control-spring-security
Access control demo project utilizing [Spring Security](http://projects.spring.io/spring-security) in a Spring Boot application. Shows how to safely load user data from a database without using potentially faked frontend values. After launching, open the web application in your browser at **http://localhost:8080**.

##crypto-spring
Crypto demo project using [Jasypt](http://www.jasypt.org) to secure [Spring](http://spring.io) configuration (property) files. Requires a system property **APP_ENCRYPTION_PASSWORD** with the value **spring-jasypt** present on startup (set automatically by the Tomcat7 Maven plugin). After launching, open the web application in your browser at **http://localhost:8080/crypto-spring**.

##csp-spring-security
Spring Boot based web application using a Content Security Policy (CSP) header. After launching, open the web application in your browser at **http://localhost:8080**.

##csrf-spring-security
Cross-Site Request Forgery (CSRF) demo project preventing CSRF in a JavaServer Pages (JSP) web application by utilizing [Spring Security](http://projects.spring.io/spring-security). After launching, open the web application in your browser at **http://localhost:8080/csrf-spring-security**. 

##csrf
Cross-Site Request Forgery (CSRF) demo project preventing CSRF in a JavaServer Pages (JSP) web application. Sample code is based on the [Enterprise Security API (ESAPI)](https://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API). After launching, open the web application in your browser at **http://localhost:8080/csrf**.

##direct-object-references
Direct object references (and indirect object references) demo project using plain Java. Sample code is based on the [Enterprise Security API (ESAPI)](https://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API). After launching, open the web application in your browser at **http://localhost:8080/direct-object-references**.

##intercept-me
Spring Boot based web application to experiment with [OWASP ZAP](https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project) as intercepting proxy. Target is to receive **SUCCESS** from the backend. After launching, open the web application in your browser at **http://localhost:8080**.

##security-header
Security response header demo project which applies **X-Content-Type-Options**, **Cache-Control**, **X-Frame-Options**, **HTTP Strict Transport Security (HSTS)**, **X-XSS-Protection** and **Content Security Policy (CSP)** (Level 1 and 2) headers to HTTP responses. After launching, open the web application in your browser at **http://localhost:8080/security-header** or **https://localhost:8443/security-header**.

##security-logging
Security logging demo project based on the [OWASP Security Logging Project](https://www.owasp.org/index.php/OWASP_Security_Logging_Project). After launching, open the web application in your browser at **http://localhost:8080/security-logging**.

##session-handling-spring-security
Session handling demo project utilizing [Spring Security](http://projects.spring.io/spring-security). Shows how to restrict access to resources (URLs), how to apply method level security and how to securely store and verify passwords. Uses Spring Security for all security related functionality. Requires a web server with Servlet 3.1 support. After launching, open the web application in your browser at **http://localhost:8080/session-handling-spring-security**.

##session-handling
Session handling demo project using plain Java. Uses plain Java to create and update the session id after logging in. Requires a web server with Servlet 3.1 support. After launching, open the web application in your browser at **http://localhost:8080/session-handling**.

##sql-injection
Spring Boot based web application to experiment with normal (vulnerable) statements, statements with escaped input, and prepared statements. After launching, open the web application in your browser at **http://localhost:8080**.

##xss
Cross-Site Scripting (XSS) demo project preventing XSS in a JavaServer Pages (JSP) web application by utilizing input validation, output escaping with [OWASP Java Encoder](https://www.owasp.org/index.php/OWASP_Java_Encoder_Project) and the [Content Security Policy (CSP)](http://www.w3.org/TR/CSP). After launching, open the web application in your browser at **http://localhost:8080/xss**.

#Command Line Applications in Detail
All projects contain **main** methods to get started.

##crypto-hash
Crypto demo project using Java to hash passwords with different hashing algorithms. All classes contain **main** methods to execute the samples. 

##crypto-java
Crypto demo project using plain Java to encrypt and decrypt data with asymmetric (RSA) and symmetric (AES) keys as well as to sign and verify data (DSA). All classes contain **main** methods to execute the samples. 

##crypto-keyczar
Crypto demo project using [Keyczar](http://www.keyczar.org) to encrypt and decrypt data with asymmetric (RSA) and symmetric (AES) keys as well as to sign and verify data (DSA). All classes contain **main** methods to execute the samples. 

##crypto-shiro
Crypto demo project using [Apache Shiro](http://shiro.apache.org) to encrypt and decrypt data with symmetric (AES) keys as well as hash data (passwords). All classes contain **main** methods to execute the samples. 

##Meta
[![Build Status](https://travis-ci.org/dschadow/JavaSecurity.svg)](https://travis-ci.org/dschadow/JavaSecurity)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)