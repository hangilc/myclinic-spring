#!/bin/bash

echo "Preparing for myclinic services"
read -p "Have you prepared sql data in data/sql? [Y/n] " YN
case "$YN" in
	[Yy]* | "" ) ;;
	"" )  ;;
	* ) exit 1 ;;
esac
read -p "Run prepare-config.sh? (data/config) [Y/n] " YN
case "$YN" in
	[Yy]* | "" ) ./prepare-config.sh ;;
esac
read -p "Compile jars? [Y/n] " YN
case "$YN" in
	[Yy]* | "" ) ./compile-jars.sh ;;
esac
cp ../server/target/server-1.0.0-SNAPSHOT.jar data/server.jar







