FastChat 
========

Travis:
[![Build Status](https://travis-ci.org/OHopiak/FastChat.svg?branch=master)](https://travis-ci.org/OHopiak/FastChat)

Jenkins:
[![Build Status](http://18.194.57.194:8080/job/FastChat/job/master/4/badge/icon)](http://18.194.57.194:8080/job/FastChat/job/master/4/)

   This is the simple chatting tool  

Requirements
------------

* Java 8 or higher
* Gradle 4.4 or higher


Building
--------
```bash
# To build the project run:
gradle build
```

Running
-------

#### On Linux:
```bash
# To start the client run:
./scripts/client

# To start the server run:
./scripts/server
```

#### On Windows:
```cmd
rem To start the client:
cd sripts
client

rem To start the server:
cd sripts
server
```

#### Using jar directly
```bash
cd build/libs

# To start the client run:
java -jar FastChat-version.jar

# To start the server run:
./scripts/server
java -jar FastChat-version.jar -s

```
 
Other branches:
 
 * [moving-to-tcp](moving-to-tcp/README.md)
