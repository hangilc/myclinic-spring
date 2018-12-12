import mysql.connector
import os

connection = None
cur = None

def open():
    global connection
    global cur
    config = {
        "user": os.environ["MYCLINIC_DB_ADMIN_USER"],
        "password": os.environ["MYCLINIC_DB_ADMIN_PASS"],
        "database": "myclinic"
    }
    connection = mysql.connector.connect(**config)
    cur = connection.cursor()

def close():
    cur.close()
    connection.close()

def get_cur():
    return cur

def get_table_names():
    cur.execute("show tables")
    return [ row[0] for row in cur ]

def has_auto_increment(extra):
    return "auto_increment" in extra

def get_column_specs(table):
    cur.execute("show columns in " + table)
    map = {}
    for row in cur:
        colname = row[0]
        map[colname] = {
            "column_name": colname,
            "db_type": row[1],
            "is_identity": has_auto_increment(row[5])
            }
    return map

def get_db_info():
    map = {}
    for table in get_table_names():
        map[table] = {
            "table_name": table,
            "columns": get_column_specs(table)
            }
    return map

open()

if __name__ == "__main__":
    import json
    print(json.dumps(get_db_info(), indent=4))