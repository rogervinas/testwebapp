# testwebapp

This is a Test Web Application using [com.sun.net.httpserver.HttpServer](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html) and [ReactiveX for Java](https://github.com/ReactiveX/RxJava).

The HttpServer handles requests by executing the HttpContext/HttpHandler configured for the requested path.
This application uses a single HttpHandler and sends every request via a RxJava Observable stream.
Then, following the MVC pattern, for each route there will be a controller subscribed at the end of the Observable stream, most likely with mapping and filtering in between.

Example of the GET/* route to serve pages to an authorized user:

```
( -----R-----R--R-----R-----R-------R------R------|-> )
.filter( method/path = GET/* )
.filter( page exists )
.map( if found, extract session from request )
.filter( session exists and is not expired )
.filter( session's user is authorized to access page )
.subscribe( controller to serve page )
```

# build

To build:

```
$ git clone git@github.com:rogervinas/testwebapp
$ cd testwebapp
$ mvn install -DskipTests=true
```

# test

To execute all tests:

```
$ mvn test [-Dtest=testName]
```

To execute one test:

```
$ mvn test -Dtest=testName
```

Where testName can be one of the following:

* TestMain: tests the basic cycle login / logout / page not found / page forbidden
* TestSessionExpiration : sets the default expiration time to 5 seconds and tests if credentials are required once the session expires
* TestConcurrency : tests the basic cycle for multiple clients connected at once