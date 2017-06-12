Test case for Ratpack handling startup failure
==============================================

According to [Ratpack](https://ratpack.io/) API documentation for
[ratpack.service.Service](https://ratpack.io/manual/1.4.6/api/ratpack/service/Service.html):

> ### Errors
> 
> If an `onStart` method errors, it will prevent the “application” from launching. ... When not in development mode, the exception will be thrown from the RatpackServer.start() method. If starting the app in a “main method”, this will prevent the JVM from starting.

So if an `onStart` method of a Service throws an exception, the expected result is that the JVM exit with non-zero exit code.

Sub-folders contain Groovy scripts using different ways to start Ratpack application.

### `groovy-script/`

Ratpack runs directly from a plain Groovy script.

    cd groovy-script
    groovy run.groovy

The actual evidence matches the expected result.

### `groovy-entrypoint/`

The Groovy script using
[ratpack.groovy.GroovyRatpackMain](https://ratpack.io/manual/1.4.6/api/ratpack/groovy/GroovyRatpackMain.html).

    cd groovy-entrypoint
    groovy run.groovy

The actual evidence matches the expected result.

### `uber-jar/`

Maven project building an executable JAR using the same
[ratpack.groovy.GroovyRatpackMain](https://ratpack.io/manual/1.4.6/api/ratpack/groovy/GroovyRatpackMain.html) entrypoint.

    cd uber-jar
    mvn package
    java -jar target/ratpack-entrypoint.jar

The actual result: the JVM does not terminate. `jconsole` shows a thread that remains:

    Name: ratpack-compute-1-1
    State: RUNNABLE
    Total blocked: 1  Total waited: 0

    Stack trace: 
    io.netty.channel.epoll.Native.epollWait0(Native Method)
    io.netty.channel.epoll.Native.epollWait(Native.java:117)
    io.netty.channel.epoll.EpollEventLoop.epollWait(EpollEventLoop.java:231)
    io.netty.channel.epoll.EpollEventLoop.run(EpollEventLoop.java:255)
    io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:873)
    ratpack.exec.internal.DefaultExecController$ExecControllerBindingThreadFactory.lambda$newThread$0(DefaultExecController.java:136)
    ratpack.exec.internal.DefaultExecController$ExecControllerBindingThreadFactory$$Lambda$122/497479191.run(Unknown Source)
    io.netty.util.concurrent.DefaultThreadFactory$DefaultRunnableDecorator.run(DefaultThreadFactory.java:144)
    java.lang.Thread.run(Thread.java:745)

