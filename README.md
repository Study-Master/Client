Study Master Client
===

## Build

You have to install JavaFX runtime as a local jar so that you can reference it in maven pom.xml file. If you have installed jdk7, actually it is easy on Mac OS. Simply start a new terminal session and type

```Bash
mvn install:install-file -Dfile="/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/jfxrt.jar" -DgroupId=com.oracle.javafx -DartifactId=javafx -Dversion=2.2 -Dpackaging=jar
```

and maven will do the installation automatically. Note that `-Dfile` refers to your JavaFX runtime jar file. Installing on Windows is quite similar. Just run this command in a new CMD session. But do remember you have to change `-Dfile` to your own settings.

You also have to install Java Websocket package. Simply run

```Bash
git clone https://github.com/Study-Master/Java-WebSocket.git && cd Java-WebSocket
mvn install
```

Now you can build the project with

```Bash
mvn clean package
```

Just be sure you are under Studymaster Client project directory.

This command will build a excutable jar file unser target directory. You may run it with double click or command

```Bash
java -jar $target_module/target/$target-client.jar
```