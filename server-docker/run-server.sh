#!/bin/bash

usage () {
    cat <<'EOS'
usage: run-server.sh [options]
    -h | --host DATABASE-HOST 
    -P | --port DATABASE-PORT (default: 3306)
    -u | --user DATABASE-USER (default: $MYCLINIC_DB_USER)
    -p | --pass DATABASE-Pass (default: $MYCLINIC_DB_PASS)
    --server-port PORT (default: 18080)
    -n | --name CONTAINER-NAME (default: myclinic-server)
    --help
EOS
}

DbHost=""
DbPort=3306
DbUser="$MYCLINIC_DB_USER"
DbPass="$MYCLINIC_DB_PASS"
Port=18080
Name="myclinic-server"

while [ $# -gt 0 ]
do
    case "$1" in
        -h | --host) DbHost="$2"
            shift
            ;;
        -P | --port) DbPort="$2"
            shift
            ;;
        -u | --user) DbUser="$2"
            shift
            ;;
        -p | --pass) DbPass="$2"
            shift
            ;;
        --server-port) Port="$2"
            shift
            ;;
        -n | --name) Name="$2"
            shift
            ;;
        --help) usage
            exit 1
            ;;
        -*) echo "Unknown option $1"
            usage
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
if ! [ -d "data/config" ]; then
    echo "Cannot find config in data/config"
    exit 1
fi
if ! [ -f "data/server.jar" ]; then
    echo "Cannot find data/server.js"
    exit 1
fi
docker create \
    --name "$Name" \
    -w /usr/src/myapp \
    -e MYCLINIC_DB_HOST="$DbHost" \
    -e MYCLINIC_DB_USER="$DbUser" \
    -e MYCLINIC_DB_PASS="$DbPass" \
    -p ${Port}:18080 \
    openjdk:11-jre java -jar server.jar

docker cp ./data/config "$Name":/usr/src/myapp/config
docker cp ./data/server.jar "$Name":/usr/src/myapp/server.jar
docker start "$Name"

