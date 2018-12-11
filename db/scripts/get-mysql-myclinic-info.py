import mysql.connector
import os
import json

def connect():
	config = {
		"user": os.environ["MYCLINIC_DB_ADMIN_USER"],
		"password": os.environ["MYCLINIC_DB_ADMIN_PASS"],
		"database": "myclinic"
	}
	return mysql.connector.connect(**config)

def get_table_names(cur):
	cur.execute("show tables")
	return [ row[0] for row in cur ]

def has_auto_increment(extra):
	return "auto_increment" in extra

def get_column_specs(cur, table):
	cur.execute("show columns in " + table)
	return [ (row[0], row[1], has_auto_increment(row[5])) for row in cur ]

if __name__ == "__main__":
	conn = connect()
	cur = conn.cursor()
	table_names = get_table_names(cur)
	tables = []
	for tablename in table_names:
		colspec = get_column_specs(cur, tablename)
		tables.append({"tablename": tablename, "columns": colspec})
	print(json.dumps({
			"tables": tables
		}, indent=4))
	cur.close()
	conn.close()