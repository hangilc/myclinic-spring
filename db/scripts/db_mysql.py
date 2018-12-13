import db_base
import mysql.connector
import os

class DbMySQL(db_base.DbBase):

    def __init__(self):
        config = {
        "user": os.environ["MYCLINIC_DB_ADMIN_USER"],
        "password": os.environ["MYCLINIC_DB_ADMIN_PASS"],
        "database": "myclinic"
        }
        connection = mysql.connector.connect(**config)
        db_base.DbBase.__init__(self, connection)

    def get_table_names(self):
        return self.execute_values("show tables")

    def get_column_specs(self, table):
        map = {}

        def has_auto_increment(extra):
            return "auto_increment" in extra

        def proc(row):
            colname = row[0]
            map[colname] = {
                "column_name": colname,
                "db_type": row[1],
                "is_identity": has_auto_increment(row[5])
                }

        self.execute_cvt("show columns in " + table, proc)
        return map

    def get_db_info(self):
        map = {}
        for table in self.get_table_names():
            map[table] = {
                "table_name": table,
                "columns": self.get_column_specs(table)
                }
        return map
