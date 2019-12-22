#!/bin/bash
set -eu

usage () {
    cat << 'EOS'
usage: start-mysql-master.sh [options]
    --root-pass ROOT-PASSWORD
    -P | --port PORT (default: 3306)
    -u | --user USER
    -p | --pass PASSWORD
    -n | --name CONTAINER-NAME (default: mysql-master)
    -s | --source INITIAL-SQL-DATA (optional)
    --help
EOS
}

DbRootPass="$MYCLINIC_DB_ROOT_PASS"
DbUser="$MYCLINIC_DB_USER"
DbPass="$MYCLINIC_DB_PASS"
Port=3306
Name="mysql-master"
DataSource=""

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
        -p | --pass) 
            DbPass="$2"
            shift 
            ;;
        -n | --name)
            Name="$2"
            shift
            ;;
        -s | --source)
            DataSource="$2"
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

if [ -z "$Name" ]
then
    echo "Container name not specified."
    usage
    exit 1
fi

docker create \
    --name "$Name" \
    -e MYSQL_ROOT_PASSWORD="$DbRootPass" \
    -e MYSQL_DATABASE=myclinic \
    -e MYSQL_USER="$DbUser" \
    -e MYSQL_PASSWORD="$DbPass" \
    -p "${Port}:3306" \
    centos/mysql-57-centos7

docker cp ./master.cnf ${Name}:/etc/my.cnf.d/60-master.cnf

docker start $Name

if [ -n "$DataSource" ]; then
    if ! ([ -f "$DataSource" ] || [ -d "$DataSource" ]); then
        echo "No such file or directory: $DataSource"
        exit 1
    fi
    while ! mysql -h 127.0.0.1 -P "$Port" -u "$DbUser" -p"$DbPass" \
        -e "select 2" myclinic 2>/dev/null 1>/dev/null
    do
        echo "waiting for server up..."
        sleep 4
    done

    if [ -f "$DataSource" ]; then
        echo "Loading $DataSource"
        MYSQL_PWD="$DbRootPass" mysql -h 127.0.0.1 -P "$Port" -u root \
            myclinic <"$DataSource" 
    fi
    if [ -d "$DataSource" ]; then
        for f in "$DataSource"/*.sql
        do
            echo "Loading $f"
            MYSQL_PWD="$DbRootPass" mysql -h 127.0.0.1 -P "$Port" -u root \
                myclinic <"$f" 
        done
    fi
fi
