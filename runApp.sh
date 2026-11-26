#!/bin/bash

#put commands here to compile and run your app from command line
set -e
mkdir -p out
javac -d out src/main/*.java
java -cp out main.MainMenu