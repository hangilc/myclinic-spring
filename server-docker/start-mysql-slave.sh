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
  --slave-port SLAVE-PORT (default: 13306)
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
DownloadTmpFile="./data/slave-download.sql"
SqlTmpFile="./data/slave.sql"
ConfTmpFile="./data/slave.cnf"
SlavePort=13306
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
        -u "$MasterUser" -e "show slave hosts" --silent --skip-column-names \
        | grep -Po '^\d+')
    if [ -n "$result" ];then
        echo "$result"
    fi
    if [ -z "$result" ]; then
        SlaveId=$(($MasterId + 1))
    else
        maxId=$(echo $result | sed 's/ /\n/g' | sort -nr | head -1)
        SlaveId=$(($maxId + 1))
    fi
}

MasterId=""
get_master_id
if [ -z "$MasterId" ]; then
    echo "Cannot get master server id"
    exit 1
fi
echo "MasterID $MasterId"

if [ -z "$SlaveId" ]; then
    select_slave_id
    if [ -z "$SlaveId" ]; then
        echo "Cannot select slave server id."
        exit 1
    fi
fi
echo "SlaveId $SlaveId"

if [ -z "$MasterHost" ]; then
    echo "Master host not specified."
    usage
    exit 1
fi
if [ "$MasterHost" == "localhost" ] || [ "$MasterHost" == "127.0.0.1" ]; then
    echo "Master host address cannot be local, or 127.0.0.1"
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

sed -e s/\${DbSlaveServerId}/"$SlaveId"/g \
    <slave-template.cnf >"$ConfTmpFile"

sed -e s/\${DbMasterHost}/"$MasterHost"/g \
    -e s/\${DbMasterPort}/"$MasterPort"/g \
    -e s/\${DbMasterUser}/"$MasterUser"/g \
    -e s/\${DbMasterPass}/"$MasterPass"/g \
    <slave-template.sql >"$SqlTmpFile"

echo "downloading master data to $DownloadTmpFile"
MYSQL_PWD="$MasterPass" mysqldump -h "$MasterHost" -P "$MasterPort" \
    -u "$MasterUser" --single-transaction --master-data myclinic \
    >"$DownloadTmpFile"

docker create \
    --name "$Name" \
    -e MYSQL_ROOT_PASSWORD="$SlaveRootPass" \
    -e MYSQL_DATABASE=myclinic \
    -e MYSQL_USER="$SlaveUser" \
    -e MYSQL_PASSWORD="$SlavePass" \
    -p "${SlavePort}":3306 \
    centos/mysql-57-centos7
    
docker cp data/slave.cnf "$Name":/etc/my.cnf.d/70-slave.cnf

docker start "$Name"

while ! mysql -h 127.0.0.1 -P "$SlavePort" -u "$SlaveUser" -p"$SlavePass" \
    -e "select 2" myclinic 2>/dev/null 1>/dev/null
do
    echo "waiting for server up..."
    sleep 4
done

echo "Loading $SqlTmpFile"
MYSQL_PWD="$SlaveRootPass" mysql -h 127.0.0.1 -P "$SlavePort" -u root \
    myclinic <"$SqlTmpFile"
echo "Loading $DownloadTmpFile"
MYSQL_PWD="$SlaveRootPass" mysql -h 127.0.0.1 -P "$SlavePort" -u root \
    myclinic <"$DownloadTmpFile" 
echo "Starting slave"
MYSQL_PWD="$SlaveRootPass" mysql -h 127.0.0.1 -P "$SlavePort" -u root \
    -e "start slave"

rm "$DownloadTmpFile"
rm "$SqlTmpFile"
rm "$ConfTmpFile"

