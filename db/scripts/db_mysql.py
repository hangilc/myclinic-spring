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
    global connection
    global cur
    cur.close()
    connection.close()

def get_cur():
    global cur
    return cur

def get_table_names():
	get_cur().execute("show tables")
	return [ row[0] for row in cur ]

def has_auto_increment(extra):
	return "auto_increment" in extra

def get_column_specs(table):
	get_cur().execute("show columns in " + table)
	return [ (row[0], row[1], has_auto_increment(row[5])) for row in cur ]

def get_db_info():
	result = []
	for table in get_table_names():
		result.append({
			"table_name": table,
			"columns": get_column_specs(table)
			})
	return result

open()

if __name__ == "__main__":
	import json
	print(json.dumps(get_db_info(), indent=4))