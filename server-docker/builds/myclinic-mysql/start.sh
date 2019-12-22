#!/bin/bash

if [ -n "$(ls /data/sql/* 2>/dev/null)" ]
then
    cp /data/sql/* /docker-entrypoint-initdb.d/
fi

if [ -n "$(ls /data/cnf/* 2>/dev/null)" ]
then
    cp /data/cnf/* /etc/mysql/mysql.conf.d/
fi

/myclinic-entrypoint.sh mysqld

