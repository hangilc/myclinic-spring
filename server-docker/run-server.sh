#!/bin/bash

usage () {
    cat <<'EOS'
usage: run-server.sh [options]
    -h | --host DATABASE-HOST
    -u | --user DATABASE-USER
    -p | --pass DATABASE-Pass
    --help
EOS
}

DbUser="$MYCLINIC_DB_USER"
DbPass="$MYCLINIC_DB_PASS"
Port=18080

while [ $# -gt 0 ]
do
    case "$1" in
        -h | --host) DbHost="$2"
            shift
            ;;
        -u | --user) DbUser="$2"
            shift
            ;;
        -p | --pass) DbPass="$2"
            shift
            ;;
        --help) usage
            exit 1
            ;;
    esac
    shift
done
   
if [ -z "$DbHost" ]; then
    echo "Database host is not specified."
    usage
    exit 1
fi

if [ -z "$DbUser" ]; then
    echo "Database user is not specified."
    usage
    exit 1
fi

if [ -z "$DbPass" ]; then
    echo "Database password is not specified."
    usage
    exit 1
fi

echo "DbHost $DbHost"
echo "DbUser $DbUser"

docker run -d \
    -v ${PWD}/data/config:/data/config \
    -v ${PWD}/data/server.jar:/data/jar/server.jar \
    -w /usr/src/myapp \
    -e MYCLINIC_DB_HOST="$DbHost" \
    -e MYCLINIC_DB_USER="$DbUser" \
    -e MYCLINIC_DB_PASS="$DbPass" \
    -p ${Port}:18080 \
    myclinic-server


