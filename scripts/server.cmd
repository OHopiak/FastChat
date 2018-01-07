@echo off
rem
rem   this script is is incomplete and won't work perfectly
rem   it should be started from scripts folder
rem

set JAR_NAME=FastChat-0.0.1.jar
cd ..
gradle jar
cd build/libs
java -jar %JAR_NAME% -s %1 %2