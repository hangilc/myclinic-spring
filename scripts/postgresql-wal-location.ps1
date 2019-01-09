Param(
    [string][alias('Host')]$DbHost = "localhost"
)

psql -h $DbHost -c "select pg_current_wal_lsn(), pg_current_wal_insert_lsn()" myclinic postgres