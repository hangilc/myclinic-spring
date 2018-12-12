import psycopg2
import os

class DbPostgreSQL:
    connection = None

    def open():
        pass

    def set_autocommit(autocommit):
        connection.autocommit = autocommit