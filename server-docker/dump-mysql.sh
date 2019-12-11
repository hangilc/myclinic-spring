#!/bin/bash

usage(){
    cat << EOS
usage: dump-mysql [options] HOST
    -u | --user User
    -p | --pass Password
    -m | --master-data (adds master data)
    -c | --charset CHARACTER-SET
    -o | --output OUTPUT-FILE
EOS
}

DbUser="root"
DbPass="$MYCLINIC_DB_ROOT_PASS"
MasterData=""
Charset=""
OutFile=""

for OPT in "$@"
do
    case $OPT in
        --help) usage
            exit 1
            ;;
        -u | --user) DbUser="$2"
            shift
            ;;
        -p | --pass) DbPass="$2"
            shift
            ;;
        -m | --master-data) MasterData="--master-data"
            ;;
        -c | --charset) Charset="$2"
            shift
            ;;
        -o | --out) OutFile="$2"
            shift
            ;;
        *) break ;;
    esac
    shift
done

DbHost="$1"

if [ -z "$DbHost" ]
then
    echo "Error: Host is not specified."
    usage
    exit 1
fi

if [ -z "$DbUser" ]
then
    echo "Error: User is not specified."
    usage
    exit 1
fi

if [ -z "$DbPass" ]
then
    echo "Error: Password is not specified."
    usage
    exit 1
fi

if [ -n "$Charset" ]
then
    CharsetOpt="--default-character-set $Charset"
fi

if [ -n "$OutFile" ]
then
    OutFileOpt="--result-file=$OutFile"
fi

mysqldump -h "$DbHost" -u "$DbUser" -p"$DbPass" \
    --single-transaction \
    $MasterData $CharsetOpt $OutFileOpt \
    myclinic

