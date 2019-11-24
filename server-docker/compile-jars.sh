#!/bin/bash

docker run -it --rm -v "$PWD/..:/usr/src/mymaven" -v "$HOME/.m2:/root/.m2" -w "/usr/src/mymaven" \
	maven:3.6.2-jdk-11 mvn clean install

