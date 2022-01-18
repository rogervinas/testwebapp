[![CI](https://github.com/rogervinas/testwebapp/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/rogervinas/testwebapp/actions/workflows/maven.yml)
[![CodeQL](https://github.com/rogervinas/testwebapp/actions/workflows/codeql-analysis.yml/badge.svg?branch=master)](https://github.com/rogervinas/testwebapp/actions/workflows/codeql-analysis.yml)

# testwebapp

**testwebapp** is a web app developed with [**HttpServer**](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html) and [**ReactiveX for Java**](https://github.com/ReactiveX/RxJava):

* A single [**HttpHandler**](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpHandler.html) is registered to the root path **/** and publishes every request to an [**Observable Stream**](http://reactivex.io/RxJava/javadoc/rx/Observable.html).
* Each route is configured with a **controller** subscribed at the end of the [**Observable Stream**](http://reactivex.io/RxJava/javadoc/rx/Observable.html). 
* Like most web application frameworks the **MVC** pattern is used.

Example of the GET route to serve pages to an authorized user, being each R one HTTP request:

```
( -----R-----R--R-----R-----R-------R------R------|-> )
.filter( method/path = GET/* )
.filter( page exists )
.map( extract session from request, if found )
.filter( session exists and is not expired )
.filter( session's user is authorized to access page )
.subscribe( controller to serve page )
```

# model

The abstract model uses **ActiveRecord** pattern and only a **In-Memory** implementation is provided. Other future implementations could use a proper **Database**. 

The following data is loaded:

* 10 roles named PAGE_1, PAGE_2, ..., PAGE_10. 
* 10 pages with path /page1, /page2, ..., /page10.
* 10 users named USER_1, USER_2, ..., USER_10.
* USER_**i** has:
	* password pass**i**
	* roles PAGE_1, PAGE_2, ..., PAGE_**i**
* Users with role PAGE_**i** have access to /page**i**.

So for example, **USER_3**:

* Has password pass3.
* Has roles PAGE_1, PAGE_2 and PAGE_3.
* Can access to /page1, /page2 and /page3.
* Cannot access to /page4, /page5, ..., /page10.

# build

To clone repository via https:
```
$ git clone --branch master https://github.com/rogervinas/testwebapp.git
```
To clone repository via ssh:
```
$ git clone --branch master git@github.com:rogervinas/testwebapp.git
```
To Build:
```
$ cd testwebapp
$ mvn install -DskipTests=true
```

# run

Using Java 8 or higher:

```
$ cd testwebapp/target
$ java [options] -jar test-web-application-1.0.1-SNAPSHOT-standalone.jar
```

Options:

```
-Dport=<port>            : port to bind HTTP server (by default 8080)
-DsessionMaxAge=<value>  : session max age in seconds (by default 5 minutes)
```

Using default options, once started the web application should be available at [http://localhost:8080](http://localhost:8080)

# test

To execute all tests:

```
$ mvn test
```

To execute one particular test:

```
$ mvn test -Dtest=<TestClass>
```

TestClass:

* **TestMain**: single client executing basic test cases (login, logout, page not found, page forbidden, ...).
* **TestSessionExpiration** : sessionMaxAge set to 5 seconds to validate session expiration.
* **TestConcurrency** : multiple clients executing basic test cases at the same time.
