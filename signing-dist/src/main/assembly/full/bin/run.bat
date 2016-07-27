@echo off

cd %0\..\..

java -classpath .;lib/*;conf ApplicationMain %*
