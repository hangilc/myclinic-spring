#!/bin/bash

set -eu

usage () {
    cat <<'EOS'
usage: start-mysql-master-from.sh [options]
  --source-host SOURCE-HOST
  --source-port SOURCE-PORT (default: 3306)
  --source-user SOURCE-USER (default: root)
  --source-pass SOURCE-PASSWORD (default: $MYCLINIC_DB_ROOT_PASS)
  --help (prints help)
EOS
}

DbSourceHost=""
DbSourcePort=3306
DbSourceUser=root
DbSourcePass="$MYCLINIC_DB_ROOT_PASS"
TmpFile="./data/download.sql"

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
echo "downloading source data to $TmpFile"
MYSQL_PWD="$DbSourcePass" mysqldump -h "$DbSourceHost" -P "$DbSourcePort" \
    -u "$DbSourceUser" --single-transaction --master-data myclinic \
    >"$TmpFile"

