Java Security
============
This repository contains several Java (web) application projects covering different security topics. Using Mozilla Firefox as browser is strongly recommended. The smaller web applications all come with a ready to use **Tomcat7 Maven plugin** which should be started via **mvn tomcat7:run-war**. Non web applications contain **main** methods to get started.

#CSRF
Cross-Site Request Forgery (CSRF) demo project preventing CSRF in a JavaServer Pages (JSP) web application (sample code is based on the [Enterprise Security API (ESAPI)](https://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API)).

#CSRF-spring-security
Cross-Site Request Forgery (CSRF) demo project preventing CSRF in a JavaServer Pages (JSP) web application by utilizing [Spring Security](http://projects.spring.io/spring-security). Use user **csrf** with password **csrf** to login.

#FerrisWheelManager
Ferris Wheel Manager is a JEE 7 demo application containing security vulnerabilities like **SQL Injection** and **Cross-Site Scripting (XSS)**. A datasource named **jdbc/fwm** is required, which must contain the tables/ data provided by SQL scripts in the [Resources](https://github.com/dschadow/JavaSecurity/tree/master/Resources/FerrisWheelManager) project. 
Valid usernames/passwords are **Marvin/wheel** (role **Manager**), **Zaphod/ferris** (role **User**).
This web application was tested with [Java Enterprise Edition 7](http://www.oracle.com/technetwork/java/javaee),  [GlassFish 4](https://glassfish.java.net) and [MySQL 5.6](http://dev.mysql.com).

#XSS
Cross-Site Scripting (XSS) demo project preventing XSS in a JavaServer Pages (JSP) web application by utilizing [OWASP Java Encoder](https://www.owasp.org/index.php/OWASP_Java_Encoder_Project) and the [Content Security Policy (CSP)](http://www.w3.org/TR/CSP).

#crypto-java
Crypto sample project using plain Java to encrypt and decrypt data with asymmetric (RSA) and symmetric (AES) keys as well as to sign and verify data (DSA). All classes contain **main** methods to execute the sample. 

#crypto-keyczar
Crypto sample project using [Keyczar](http://www.keyczar.org) to encrypt and decrypt data with asymmetric (RSA) and symmetric (AES) keys as well as to sign and verify data (DSA). All classes contain **main** methods to execute the sample. 

#crypto-shiro
Crypto sample project using [Apache Shiro](http://shiro.apache.org) to encrypt and decrypt data with symmetric (AES) keys as well as hash data (passwords). All classes contain **main** methods to execute the sample. 

#crypto-spring
Crypto sample project using [Jasypt](http://www.jasypt.org) to secure [Spring](http://spring.io) configuration (property) files. Requires a system property **APP_ENCRYPTION_PASSWORD** with the value **spring-jasypt** present on startup (set automatically by the Tomcat7 Maven plugin).
