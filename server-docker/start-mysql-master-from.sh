#!/bin/bash

set -eu

usage () {
    cat <<'EOS'
usage: start-mysql-master-from.sh [options]
  --source-host SOURCE-HOST
  --source-port SOURCE-PORT (default: 3306)
  --source-user SOURCE-USER (default: root)
  --source-pass SOURCE-PASSWORD (default: $MYCLINIC_DB_ROOT_PASS)
  --root-pass ROOT-PASSWORD (default: $MYCLINIC_DB_ROOT_PASS)
  -P | --port PORT (default: 3306)
  -u | --user USER (default: $MYCLINIC_DB_USER)
  -p | --pass PASSWORD (default: $MYCLINIC_DB_PASS)
  -n | --name CONAINER-NAME (default: mysql-master)
  --help (prints help)
EOS
}

DbSourceHost=""
DbSourcePort=3306
DbSourceUser=root
DbSourcePass="$MYCLINIC_DB_ROOT_PASS"
TmpFile="./data/download.sql"
RootPass="$MYCLINIC_DB_ROOT_PASS"
Port="3306"
User="$MYCLINIC_DB_USER"
Pass="$MYCLINIC_DB_PASS"
Name="mysql-master"

while [ $# -gt 0 ]
do
    case "$1" in
        --source-host) DbSourceHost="$2"
            shift
            ;;
        --source-port) DbSourcePort="$2"
            shift
            ;;
        --source-user) DbSourceUser="$2"
            shift
            ;;
        --source-pass) DbSourcePass="$2"
            shift
            ;;
        --root-pass) RootPass="$2"
            shift
            ;;
        -P | --port) Port="$2"
            shift
            ;;
        -u | --user) User="$2"
            shift
            ;;
        -p | --pass) Pass="$2"
            shift
            ;;
        -n | --name) Name="$2"
            shift
            ;;
        --help) usage
            exit 1
            ;;
        "-*") echo "Unknown option $1"
            usage
            exit 1
            ;;
    esac
    shift
done

if ! [ -d "./data" ]; then
    mkdir "./data"
fi
if [ -z "$DbSourceHost" ]; then
    echo "Source host is not specified."
    usage
    exit 1
fi
if [ -z "$DbSourceUser" ]
then
    echo "Source db user is not specified."
    usage
    exit 1
fi

if [ -z "$Name" ]
then
    echo "Container name not specified."
    usage
    exit 1
fi

echo "downloading source data to $TmpFile"
MYSQL_PWD="$DbSourcePass" mysqldump -h "$DbSourceHost" -P "$DbSourcePort" \
    -u "$DbSourceUser" --single-transaction myclinic \
    >"$TmpFile"

docker create \
    --name "$Name" \
    -e MYSQL_ROOT_PASSWORD="$RootPass" \
    -e MYSQL_DATABASE=myclinic \
    -e MYSQL_USER="$User" \
    -e MYSQL_PASSWORD="$Pass" \
    -p "${Port}:3306" \
    centos/mysql-57-centos7

docker cp ./master.cnf ${Name}:/etc/my.cnf.d/60-master.cnf

docker start $Name

while ! mysql -h 127.0.0.1 -P "$Port" -u "$User" -p"$Pass" \
    -e "select 2" myclinic 2>/dev/null 1>/dev/null
do
    echo "waiting for server up..."
    sleep 4
done

echo "Loading $TmpFile"
MYSQL_PWD="$RootPass" mysql -h 127.0.0.1 -P "$Port" -u root \
    myclinic <"$TmpFile" 

