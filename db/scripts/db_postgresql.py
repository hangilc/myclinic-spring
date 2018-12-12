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
    cur.close()
    connection.close()

def get_cur():
    return cur

def get_table_names():
    cur.execute("select tablename from pg_catalog.pg_tables where schemaname = 'public'")
    return [ row[0] for row in cur ]

def get_column_specs(table):
    cur.execute("select column_name, data_type, is_identity from information_schema.columns where table_schema = 'public' and table_name = %s ", (table,))
    map = {}
    for row in cur:
        colname = row[0]
        map[colname] = {
            "column_name": colname,
            "db_type": row[1],
            "is_identity": row[2] == 'YES'
            }
    return map

def get_foreign_key_constraints():
    cur.execute("select tc.constraint_name, kcu.table_name, kcu.column_name, ccu.table_name, ccu.column_name from information_schema.table_constraints tc join information_schema.key_column_usage kcu on tc.constraint_name = kcu.constraint_name join information_schema.constraint_column_usage ccu on tc.constraint_name = ccu.constraint_name where constraint_type = 'FOREIGN KEY'")
    return [ {
        "constraint_name": r[0],
        "referring_table": r[1],
        "referring_column": r[2],
        "referred_table": r[3],
        "referred_column": r[4]
    } for r in cur ]

def get_db_info():
    map = {}
    for table in get_table_names():
        cols = get_column_specs(table)
        map[table] = {
            "table_name": table,
            "columns": cols,
            "referring": set([]),
            "referred_by": set([])
        }
    for c in get_foreign_key_constraints():
        referred = c["referred_table"]
        referring = c["referring_table"]
        map[referred]["referred_by"].add(referring)
        map[referring]["referring"].add(referred)
    for v in map.values():
        v["referred_by"] = list(v["referred_by"])
        v["referring"] = list(v["referring"])
    return map

open()

if __name__ == "__main__":
    import json
    print(json.dumps(get_db_info(), indent=4))
