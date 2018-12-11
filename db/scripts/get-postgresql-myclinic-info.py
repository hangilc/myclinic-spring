import psycopg2
import os
import json

def connect():
    return psycopg2.connect("host=localhost dbname=myclinic " + 
        f"user={os.environ['MYCLINIC_DB_ADMIN_USER']} " + 
        f"password={os.environ['MYCLINIC_DB_ADMIN_PASS']}")

def get_table_names(cur):
    cur.execute("select tablename from pg_catalog.pg_tables where schemaname = 'public'")
    return [ row[0] for row in cur ]

def get_column_specs(cur, table):
    cur.execute("select column_name, data_type, is_identity from information_schema.columns where table_schema = 'public' and table_name = %s ", (table,))
    return [ (row[0], row[1], row[2] == 'YES') for row in cur ]

def get_table_info(cur, tablename):
	colspecs = get_column_specs(cur, tablename)
	d = {"tablename": tablename, "columns": get_column_specs(cur, tablename)}
	return d

if __name__ == "__main__":
	conn = connect()
	cur = conn.cursor()
	table_names = get_table_names(cur)
	tables = []
	for tablename in table_names:
		info = get_table_info(cur, tablename)
		tables.append(info)
	print(json.dumps({
			"tables": tables
		}, indent=4))
	cur.close()
	conn.close()