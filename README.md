# testwebapp
Test Web Application using [com.sun.net.httpserver.HttpServer](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html) and [ReactiveX for Java](https://github.com/ReactiveX/RxJava)

HttpServer handles requests by executing the HttpContext/HttpHandler configured for the requested path.

In this TestWebApp a single HttpHandler is used which sends the HttpExchange via an RxJava Observable stream:

Following MVC each configured route will have a controller subscribed at the end of this Observable stream, with mapping and filtering in between.

For example a GET to an authorized page from an authenticated user will be:

( -----R-----R--R-----R-----R-------R------R------|-> )
. filter by method/path (GET *)
. filter by page exists
. map session (extract session information from request)
. filter by session (session exists? session is not expired?)
. filter by authorization (extract user from session and check authorization)
. subscribe to controller to render page

# build

To build:

```
$ git clone git@github.com:rogervinas/testwebapp
$ cd testwebapp
$ mvn install -DskipTests = true
```

# test

To test:

```
$ mvn test [-Dtest=testName]
```

Where testName can be:

* TestMain: tests the basic cycle login / logout / page not found / page forbidden
* TestSessionExpiration : sets the default expiration time to 5 seconds and tests if credentials are required once the session expires
* TestConcurrency : tests the basic cycle for multiple clients connected at once