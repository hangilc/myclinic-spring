#!/bin/bash

docker run -d `
    -v ${PWD}/data/config:/data/config `
    -v ${PWD}/data/server.jar:/data/jar/server.jar `
    -w /usr/src/myapp `
    -e MYCLINIC_DB_HOST="$DbHost" `
    -e MYCLINIC_DB_USER="$DbUser" `
    -e MYCLINIC_DB_PASS="$DbPass" `
    -p ${Port}:18080 `
    myclinic-server


