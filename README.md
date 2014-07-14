auto-property
=================

**auto-property** is a Java annotation processor that generates some boilerplate code when dealing with value classes that use JavaFX properties as fields.

The way it works 
----------------
Create an abstract class and annotate it with `@AutoProperty`:

```java
@AutoProperty
public abstract class Model {
	public abstract StringProperty description();
}
```

Why an abstract class? 
----------------------
The idea is stolen from [Google's auto-value project][0]. It allows you to write readable code while still having the flexibility to change any behaviour if necesary.
Read the [readme of auto-value][0] for an in-depth discussion on the pros and cons of this approach. Pay special attention to the need of testing classes generated this way, which is a consequence of dependening on the order you specify the properties in.


Dependencies
------------
**auto-property** depends on:
 * Java 1.8 and 
 * StringTemplate (and as such on ANTLR).

It comes in two flavours; a __complete__ JAR with all transitive dependencies included:

	auto-property-{$version}-complete.jar
	
And a __simple__ JAR with none of the dependencies included ,in case you are already using some of them in your project. 

	auto-property-{$version}.jar
	
**Note:** Take extra care if you use the simple flavour but add newer versions of the dependencies to your classpath.

```
TODO(bugabinga): Add download links.
TODO(bugabinga): filter this file with gradle to add version number.
```

Installation
------------
Simply drop `auto-property-{$version}-complete.jar` onto your CLASSPATH and start using the `@AutoProperty` annotation. Assuming the JAR lives in your `/usr/local/lib/` directory:

	$ export CLASSPATH="/usr/local/lib/auto-property-{$version}-complete.jar:$CLASSPATH"

Maven/Gradle/Ivy and Co.
------------------------
```
TODO(bugabinga): Add maven site dependency .
```

Hacking on auto-property
------------------------
The project uses [Gradle](http://www.gradle.org/) as a build tool. As such it is relativly simple to get started using the [Gradle wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html). If you do not have gradle installed on your system use :
	$ gradlew
in the project directory to get started. 

Gradle can also generate some IDE config files if you need them to import into your IDE.

Using Eclipse
-------------
	$ gradlew eclipse
	
[Eclipse import existing project](http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Ftasks%2Ftasks-importproject.htm).

Using Intellij
--------------
	$ gradlew idea
	
[Intellij IDEA import existing project](http://confluence.jetbrains.com/display/IntelliJIDEA/Import+an+Existing+Project).
	
Testing auto-property
---------------------
Testing an annotation processor means, that one has to invoke the javac compiler and check the results dynamically. The specifics of that are abstracted away in [AbstractProcessorTest](/src/test/java/net/bugabinga/annotaion/bean/AbstarctProcessor.java).
Override the `getProcessors` method and create an instance of the processor under test:

```java
@Override
protected Collection<Processor> getProcessors() {
	return Collections.singletonList(new net.bugabinga.annotation.bean.Processor());
}
```

Then use the assertion methods `assertCompilationSuccessful` and `assertCompilationReturned` to write your tests.

```
TODO(bugabinga): Link to demo project
```

[0]: https://github.com/google/auto/tree/master/value "auto-value"
