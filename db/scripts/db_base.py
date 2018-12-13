
class DbBase:

    def __init__(self, conn):
        self.conn = conn

    def execute_proc(self, sql, proc, params =()):
        cursor = self.conn.cursor()
        cursor.execute(sql, params)
        try:
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

