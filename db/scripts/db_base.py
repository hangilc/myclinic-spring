
class DbBase:

    def __init__(self, conn, database_name):
        self.conn = conn
        self.database_name = database_name

    def execute_proc(self, sql, proc=None, params =()):
        cursor = self.conn.cursor()
        try:
            cursor.execute(sql, params)
            if proc:
                for r in cursor:
                    proc(r)
        except Exception as e:
            self.conn.rollback()
            raise e
        else:
            self.conn.commit()
        finally:
            cursor.close()

    def execute_cvt(self, sql, cvt, params=()):
        result = []
        def proc(r):
            result.append(cvt(r))
        self.execute_proc(sql, proc, params)
        return result

    def execute(self, sql, params=()):
        return self.execute_cvt(sql, lambda x: x, params)

    def execute_values(self, sql, params=()):
        return self.execute_cvt(sql, lambda x: x[0], params)

    def execute_no_result(self, sql, params=()):
        self.execute_proc(sql, None, params)

    def get_db_info(self):
        map = {}
        for table in self.get_table_names():
            cols = self.get_column_specs(table)
            map[table] = {
                "table_name": table,
                "columns": cols,
                "referring": set([]),
                "referred_by": set([])
            }
        for c in self.get_foreign_key_constraints():
            referred = c["referred_table"]
            referring = c["referring_table"]
            map[referred]["referred_by"].add(referring)
            map[referring]["referring"].add(referred)
        for v in map.values():
            v["referred_by"] = list(v["referred_by"])
            v["referring"] = list(v["referring"])
        return map

if __name__ == "__main__":
    import psycopg2
    import os
    config = {
        "host": "localhost",
        "database": "myclinic",
        "user": os.environ['MYCLINIC_DB_ADMIN_USER'],
        "password": os.environ['MYCLINIC_DB_ADMIN_PASS']
    }
    connection = psycopg2.connect(**config)
    base = DbBase(connection)
    print(base.execute("select * from patient where patient_id = %s", (200,)))

