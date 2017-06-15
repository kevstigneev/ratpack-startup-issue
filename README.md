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

### `uber-jar-groovy/`

Maven project building an executable JAR using the same
[ratpack.groovy.GroovyRatpackMain](https://ratpack.io/manual/1.4.6/api/ratpack/groovy/GroovyRatpackMain.html) entrypoint.

    cd uber-jar-groovy
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
    ratpack.exec.internal.DefaultExecController$ExecControllerBindingThreadFactory$$Lambda$122/489070295.run(Unknown Source)
    io.netty.util.concurrent.DefaultThreadFactory$DefaultRunnableDecorator.run(DefaultThreadFactory.java:144)
    java.lang.Thread.run(Thread.java:745)


### `uber-jar-java/`

Maven project building an executable JAR using the canonical
[ratpack.server.RatpackServer](https://ratpack.io/manual/1.4.6/api/ratpack/server/RatpackServer.html)
entrypoint.

    cd uber-jar-java
    mvn package
    java -jar target/ratpack-entrypoint.jar

The actual result: like the previous example the JVM does not terminate.


Environment
-----------

Ratpack version: 1.4.6

    $ uname -a
    Linux ke-laptop 4.4.0-78-generic #99~14.04.2-Ubuntu SMP Thu Apr 27 18:49:46 UTC 2017 x86_64 x86_64 x86_64 GNU/Linux

    $ java -version
    openjdk version "1.8.0_111"
    OpenJDK Runtime Environment (build 1.8.0_111-8u111-b14-3~14.04.1-b14)
    OpenJDK 64-Bit Server VM (build 25.111-b14, mixed mode)

    $ groovy -version
    Groovy Version: 2.4.6 JVM: 1.8.0_111 Vendor: Oracle Corporation OS: Linux
