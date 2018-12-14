import db_base
import mysql.connector
import os

class DbMySQL(db_base.DbBase):

    def __init__(self, database_name='myclinic'):
        config = {
        "user": os.environ["MYCLINIC_DB_ADMIN_USER"],
        "password": os.environ["MYCLINIC_DB_ADMIN_PASS"],
        "database": "myclinic"
        }
        connection = mysql.connector.connect(**config)
        db_base.DbBase.__init__(self, connection, database_name)

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

    def get_foreign_key_constraints(self):
        sql = """
            select tc.constraint_name, kcu.table_name, kcu.column_name, 
                kcu.referenced_table_name, kcu.referenced_column_name 
            from information_schema.table_constraints tc 
                join information_schema.key_column_usage kcu 
                    on tc.constraint_name = kcu.constraint_name 
            where constraint_type = 'FOREIGN KEY' and tc.table_schema = %s and
                kcu.table_schema = tc.table_schema and kcu.referenced_table_schema = tc.table_schema
            """
        def cvt(r):
            return {
                "constraint_name": r[0],
                "referring_table": r[1],
                "referring_column": r[2],
                "referred_table": r[3],
                "referred_column": r[4]
            }
        return self.execute_cvt(sql, cvt, (self.database_name,))

