#!/bin/bash

usage () {
    cat << 'EOS'
usage: start-mysql-master.sh [options]
    --root-pass ROOT-PASSWORD
    -P | --port PORT
    -s | --sql INITIAL-SQL-DATA-FILE
    -u | --user USER
    -p | --pass PASSWORD
    -c | --charset INIT-SQL-DATA-CHARSET
    --help
EOS
}

DbRootPass="$MYCLINIC_DB_ROOT_PASS"
DbUser="$MYCLINIC_DB_USER"
DbPass="$MYCLINIC_DB_PASS"
Port=3306
SqlFile="empty.sql"
Charset=""

while [ $# -gt 0 ]
do
    case "$1" in
        --root-pass) 
            DbRootPass="$2"
            shift 
            ;;
        -P | --port) 
            Port="$2"
            shift 
            ;;
        -s | --sql) 
            SqlFile="$2"
            shift 
            ;;
        -u | --user) 
            DbUser="$2"
            shift 
            ;;
        -p | --pass) 
            DbPass="$2"
            shift 
            ;;
        -c | --charset) 
            Charset="$2"
            shift 
            ;;
        --help)
            usage
            exit 1
            ;;
        -*) echo "Unknown option $1"
            usage
            exit 1
    esac
    shift
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
echo "$SqlPath"

docker run -d \
    -e MYSQL_ROOT_PASSWORD="$DbRootPass" \
    -e MYSQL_DATABASE=myclinic \
    -e MYSQL_USER="$DbUser" \
    -e MYSQL_PASSWORD="$DbPass" \
    -p "${Port}:3306" \
    -v "${PWD}/master.cnf":/etc/my.cnf.d/60-myclinic-master.cnf \
    centos/mysql-57-centos7

 
