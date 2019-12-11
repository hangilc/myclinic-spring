#!/bin/bash

usage(){
    echo << EOS
usage: start-mysql-master.sh [options]
    --root-pass ROOT-PASSWORD
    -P | --port PORT
    -s | --sql INITIAL-SQL-DATA-FILE
EOS
}

DbRootPass="$MYCLINIC_DB_ROOT_PASS"
Port=3306
SqlFile="empty.sql"

for OPT in "$@"
do
    case $OPT in
        --root-pass) DbRootPass="$2"
            shift
            ;;
        -P | --port) Port="$2"
            shift
            ;;
        -s | --sql) SqlFile="$2"
            echo $SqlFile
            shift
            ;;
    esac
done

if [ -z "$DbRootPass" ]
then
    echo "Error: Root password is empty."
    usage
    exit 1
fi

if [ ! -f "$SqlFile" ]
then
    echo "Cannof find sql file $SqlFile."
    exit 1
fi

SqlPath=$(realpath "$SqlFile")

docker run -d \
    -e MYSQL_ROOT_PASSWORD="$DbRootPass" \
    -e MYSQL_DATABASE=myclinic \
    -p "$Port":3306 \
    -v "${PWD}/master/cnf":/data/cnf \
    -v "$SqlPath":/data/sql/dump.sql \
    myclinic-mysql

