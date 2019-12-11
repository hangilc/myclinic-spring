#!/bin/bash
docker run --rm -v "${PWD}/..":/usr/src/mymaven -v ~/.m2:/root/.m2 -w "/usr/src/mymaven" maven:3.6.2-jdk-11 mvn clean install

