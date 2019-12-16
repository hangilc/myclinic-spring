#!/bin/bash

usage () {
    cat <<'EOS'
usage: upload-mysql.sh [options] HOST SQLFILE
          -u | --user DB_USER
          -p | --pass DB_PASS
          -c | --charset CHARACTER-SET
       upload-mysql.sh --help
EOS
}

User="root"
Pass="$MYCLINIC_DB_ROOT_PASS"
Charset=""

while [ $# -gt 0 ]
do
    case "$1" in
        --help) usage
            exit 1
            ;;
        -u | --user) User="$1"
            shift
            ;;
        -p | --pas) Pass="$2"
            shift
            ;;
        -c | --charset) Charset="$2"
            shift
            ;;
        *) break
            ;;
    esac
    shift
done

Host="$1"
SqlFile="$2"

if [ -z "$1" ]; then
    echo "Host not specified."
    usage
    exit 1
fi
if [ -z "$2" ]; then
    echo "Data file not specified."
    usage
    exit 1
fi

CharsetOpt=""
if [ -n "$Charset" ]; then
    CharsetOpt="--default-character-set=$Charset"
    echo "$CharsetOpt"
fi

mysql -h "$Host" -u "$User" -p"$Pass" --wait "$CharsetOpt" myclinic <"$SqlFile"  


