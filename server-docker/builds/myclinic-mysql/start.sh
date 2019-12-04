#!/bin/sh
cp /data/sql/* /docker-entrypoint-initdb.d/
cp /data/cnf/* /etc/mysql/mysql.conf.d/
/entrypoint.sh mysqld
