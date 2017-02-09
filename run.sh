#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
DIR="$DIR/src"

javac $DIR/ClientServiceThread.java $DIR/Server.java

java -cp $DIR Server