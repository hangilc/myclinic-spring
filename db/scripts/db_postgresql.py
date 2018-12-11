import psycopg2
import os

connection = None
cur = None

def open():
    global connection
    global cur
    config = {
        "host": "localhost",
        "database": "myclinic",
        "user": os.environ['MYCLINIC_DB_ADMIN_USER'],
        "password": os.environ['MYCLINIC_DB_ADMIN_PASS']
    }
    connection = psycopg2.connect(**config)
    cur = connection.cursor()

def close():
    global connection
    global cur
    cur.close()
    connection.close()

def get_cur():
    global cur
    return cur

def get_table_names():
    get_cur().execute("select tablename from pg_catalog.pg_tables where schemaname = 'public'")
    return [ row[0] for row in cur ]

def get_column_specs(table):
    get_cur().execute("select column_name, data_type, is_identity from information_schema.columns where table_schema = 'public' and table_name = %s ", (table,))
    return [ (row[0], row[1], row[2] == 'YES') for row in cur ]

open()

