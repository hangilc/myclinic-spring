import db_base
import psycopg2
import os

class DbPostgreSQL(db_base.DbBase):

    def __init__(self, database_name='myclinic'):
        config = {
            "host": "localhost",
            "database": "myclinic",
            "user": os.environ['MYCLINIC_DB_ADMIN_USER'],
            "password": os.environ['MYCLINIC_DB_ADMIN_PASS']
        }
        connection = psycopg2.connect(**config)
        db_base.DbBase.__init__(self, connection, database_name)

    def get_table_names(self):
        sql = """
            select tablename from pg_catalog.pg_tables where schemaname = 'public'
            """
        return self.execute_values(sql)

    def get_column_specs(self, table):
        sql = """
            select column_name, data_type, is_identity 
            from information_schema.columns 
            where table_schema = 'public' and table_name = %s 
            """
        map = {}

        def proc(row):
            colname = row[0]
            map[colname] = {
                "column_name": colname,
                "db_type": row[1],
                "is_identity": row[2] == 'YES'
                }

        self.execute_proc(sql, proc, (table,))
        return map
    
    def get_foreign_key_constraints(self):
        sql = """
            select tc.constraint_name, kcu.table_name, kcu.column_name, 
                ccu.table_name, ccu.column_name 
            from information_schema.table_constraints tc 
                join information_schema.key_column_usage kcu 
                    on tc.constraint_name = kcu.constraint_name 
                join information_schema.constraint_column_usage ccu 
                    on tc.constraint_name = ccu.constraint_name 
            where constraint_type = 'FOREIGN KEY'
            """
        def cvt(r):
            return {
                "constraint_name": r[0],
                "referring_table": r[1],
                "referring_column": r[2],
                "referred_table": r[3],
                "referred_column": r[4]
            }
        return self.execute_cvt(sql, cvt)

