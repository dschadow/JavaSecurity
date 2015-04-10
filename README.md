Java Security
============
This repository contains several Java web applications and command line applications covering different security topics. 

#Web Applications in Detail
Using Mozilla Firefox as browser is strongly recommended. The smaller web applications all come with a ready to use **Tomcat7 Maven plugin** which can be started via **mvn tomcat7:run-war**. The web applications which require a **Servlet 3.1** capable server contain the **Jetty Maven plugin** which can be started via **mvn jetty:run-war**.

##application-intrusion-detection

##CSRF-spring-security
Cross-Site Request Forgery (CSRF) demo project preventing CSRF in a JavaServer Pages (JSP) web application by utilizing [Spring Security](http://projects.spring.io/spring-security). After launching, open the web application in your browser at **http://localhost:8080/csrf-spring-security**. 

##CSRF
Cross-Site Request Forgery (CSRF) demo project preventing CSRF in a JavaServer Pages (JSP) web application. Sample code is based on the [Enterprise Security API (ESAPI)](https://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API). After launching, open the web application in your browser at **http://localhost:8080/csrf**.

#DirectObjectReferences
Direct object references (and indirect object references) demo project using plain Java. Sample code is based on the [Enterprise Security API (ESAPI)](https://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API). After launching, open the web application in your browser at **http://localhost:8080/DirectObjectReferences**.

#FerrisWheelManager
Ferris Wheel Manager is a JEE 7 demo application containing security vulnerabilities like **SQL Injection** and **Cross-Site Scripting (XSS)**. A datasource named **jdbc/fwm** is required, which must contain the tables/ data provided by SQL scripts in the [Resources](https://github.com/dschadow/JavaSecurity/tree/master/Resources/FerrisWheelManager) project. 
Valid usernames/passwords are **Marvin/wheel** (role **Manager**), **Zaphod/ferris** (role **User**).
This web application was tested with [Java Enterprise Edition 7](http://www.oracle.com/technetwork/java/javaee),  [GlassFish 4](https://glassfish.java.net) and [MySQL 5.6](http://dev.mysql.com).

##InterceptMe
Simple web application to experiment with **OWASP ZAP** as intercepting proxy. Target is to receive **SUCCESS** (in uppercase) from the Servlet which will be returned when the name parameter in the form is **inject** (in lowercase). After launching, open the web application in your browser at **http://localhost:8080/interceptme**.

#SQL-Injection
SQL Injection demo project using normal (vulnerable statements), statements with escaped input, prepared statements and Hibernate Query Language. After launching, open the web application in your browser at **http://localhost:8080/SQL-Injection**.
This web application creates a sample database **SQL-Injection-DB.mv.db** in the **user.home** directory. In case this file is deleted it will be recreated during the next server start.

#XSS
Cross-Site Scripting (XSS) demo project preventing XSS in a JavaServer Pages (JSP) web application by utilizing input validation, output escaping with [OWASP Java Encoder](https://www.owasp.org/index.php/OWASP_Java_Encoder_Project) and the [Content Security Policy (CSP)](http://www.w3.org/TR/CSP). After launching, open the web application in your browser at **http://localhost:8080/xss**.

#crypto-spring
Crypto demo project using [Jasypt](http://www.jasypt.org) to secure [Spring](http://spring.io) configuration (property) files. Requires a system property **APP_ENCRYPTION_PASSWORD** with the value **spring-jasypt** present on startup (set automatically by the Tomcat7 Maven plugin). After launching, open the web application in your browser at **http://localhost:8080/crypto-spring**.

#security-header
Security response header demo project which applies **X-Content-Type-Options**, **Cache-Control**, **X-Frame-Options**, **HTTP Strict Transport Security (HSTS)**, **X-XSS-Protection** and **Content Security Policy (CSP)** (Level 1 and 2) headers to HTTP responses. This project requires a web container like Apache Tomcat (included via Maven Tomcat Plug-in). After launching, open the web application in your browser at **http://localhost:8080/security-header** or **https://localhost:8443/security-header**.

#session-handling
Session handling demo project using plain Java. Uses plain Java to create and update the session id after logging in. Requires a web server with Servlet 3.1 support. After launching, open the web application in your browser at **http://localhost:8080/session-handling**.

#session-handling-spring-security
Session handling demo project utilizing [Spring Security](http://projects.spring.io/spring-security). Shows how to restrict access to resources (URLs) and how to apply method level security. Uses Spring Security to create and update the session id after logging in. After launching, open the web application in your browser at **http://localhost:8080/session-handling-spring-security**.

#Command Line Applications in Detail
All projects contain **main** methods to get started.

##crypto-java
Crypto demo project using plain Java to encrypt and decrypt data with asymmetric (RSA) and symmetric (AES) keys as well as to sign and verify data (DSA). All classes contain **main** methods to execute the samples. 

##crypto-keyczar
Crypto demo project using [Keyczar](http://www.keyczar.org) to encrypt and decrypt data with asymmetric (RSA) and symmetric (AES) keys as well as to sign and verify data (DSA). All classes contain **main** methods to execute the samples. The latest Keyczar dependency is not available in any public Maven repo so far. Download the jar and add it to your local Maven repository manually.

##crypto-shiro
Crypto demo project using [Apache Shiro](http://shiro.apache.org) to encrypt and decrypt data with symmetric (AES) keys as well as hash data (passwords). All classes contain **main** methods to execute the samples. 

##Meta
[![Build Status](https://travis-ci.org/dschadow/JavaSecurity.svg)](https://travis-ci.org/dschadow/JavaSecurity)