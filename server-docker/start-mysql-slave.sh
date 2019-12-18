#!/bin/bash

set -eu

usage () {
    cat <<'EOS'
usage: start-mysql-slave.sh [options]
  --master-host MASTER-HOST
  --master-port MASTER-PORT (default: 3306)
  --master-user MASTER-USER (default: root)
  --master-pass MASTER-PASSWORD (default: $MYCLINIC_DB_ROOT_PASS)
  --name CONTAINER-NAME (dfault: mysql-slave)
  --slave-root-pass SLAVE-ROOT-PASSWORD (default: $MYCLINIC_DB_ROOT_PASS)
  --slave-port SLAVE-PORT (default: 3306)
  --slave-user SLAVE-USER (default: $MYCLINIC_DB_USER)
  --slave-pass SLAVE-PASS (default: $MYCLINIC_DB_PASS)
  --slave-id SLAVE-SERVER-ID (default: automatically selected)
  --help (prints help)
EOS
}

MasterHost=""
MasterPort=3306
MasterUser=root
MasterPass="$MYCLINIC_DB_ROOT_PASS"
Name="mysql-slave"
TmpFile="./data/download.sql"
SlavePort=3306
SlaveRootPass="$MYCLINIC_DB_ROOT_PASS"
SlaveUser="$MYCLINIC_DB_USER"
SlavePass="$MYCLINIC_DB_PASS"
SlaveId=""

while [ $# -gt 0 ]
do
    case "$1" in
        --master-host) MasterHost="$2"
            shift
            ;;
        --master-port) MasterPort="$2"
            shift
            ;;
        --master-user) MasterUser="$2"
            shift
            ;;
        --master-pass) MasterPass="$2"
            shift
            ;;
        --name) Name="$2"
            shift
            ;;
        --slave-root-pass) SlaveRootPass="$2"
            shift
            ;;
        --slave-port) SlavePort="$2"
            shift
            ;;
        --slave-user) SlaveUser="$2"
            shift
            ;;
        --slave-pass) SlavePass="$2"
            shift
            ;;
        --slave-id) SlaveId="$2"
            shift
            ;;
        --help) usage
            exit 1
            ;;
    esac
    shift
done

function get_master_id () {
    local result=$(MYSQL_PWD="$MasterPass" mysql -h "$MasterHost" \
        -u "$MasterUser" -e "select @@server_id" --silent \
        --skip-column-names)
    MasterId="$result"
}

function select_slave_id () {
    local result=$(MYSQL_PWD="$MasterPass" mysql -h "$MasterHost" \
        -u "$MasterUser" -e "show slave hosts" )
    if [ -z "$result" ]; then
        SlaveId=$(($MasterId + 1))
    else
        echo "Cannot select slave server id. Set with --slave-server-id option."

        exit 1
    fi
}

MasterId=""
get_master_id
if [ -z "$MasterId" ]; then
    echo "Cannot get master server id"
    exit 1
fi
if [ -z "$SlaveId" ]; then
    select_slave_id
    if [ -z "$SlaveId" ]; then
        echo "Cannot select slave server id."
        exit 1
    fi
fi

if [ -z "$MasterHost" ]; then
    echo "Master host not specified."
    usage
    exit 1
fi
if ! [ -d "./data" ]; then
    mkdir "./data"
fi
if [ -z "$SlaveId" ]; then
    select_server_id
fi
if [ -z "$SlaveId" ]; then
    echo "Slave server id not specified."
    usage
    exit 1
fi

sed -e s/\${DbSlaveServerId}/"$SlaveId"/g <slave-template.cnf >./data/slave.cnf
exit 1

echo "downloading master data to $TmpFile"
MYSQL_PWD="$MasterPass" mysqldump -h "$MasterHost" -P "$MasterPort" \
    -u "$MasterUser" --single-transaction --master-data myclinic \
    >"$TmpFile"

docker run -d \
    --name "$Name" \
    -e MYSQL_ROOT_PASSWORD="$DbRootPass" \
    -e MYSQL_DATABASE=myclinic \
    -e MYSQL_USER="$DbUser" \
    -e MYSQL_PASSWORD="$DbPass" \
    -p "${Port}:3306" \
    -v "${PWD}/master.cnf":/etc/my.cnf.d/60-myclinic-master.cnf \
    centos/mysql-57-centos7

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
