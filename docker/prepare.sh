#!/bin/bash

echo "Preparing for myclinic services"
read -p "Have you prepared sql data in data/sql? [Y/n] " YN
case "$YN" in
	[Yy]* | "" ) ;;
	"" )  ;;
	* ) exit 1 ;;
esac
read -p "Run prepare-config.sh? [Y/n] " YN
case "$YN" in
	[Yy]* | "" ) ./prepare-config.sh ;;
esac


