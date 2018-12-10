import psycopg2
import os
import sys

def connect():
    return psycopg2.connect("host=localhost dbname=myclinic " + 
        f"user={os.environ['MYCLINIC_POSTGRES_USER']} " + 
        f"password={os.environ['MYCLINIC_POSTGRES_PASS']}")

def get_table_names(cur):
    cur.execute("select tablename from pg_catalog.pg_tables where schemaname = 'public'")
    return [ row[0] for row in cur ]

def get_column_names(cur, table):
    cur.execute("select column_name, data_type, is_identity from information_schema.columns where table_schema = 'public' and table_name = %s ", (table,))
    return [ row[0] for row in cur ]

def get_column_specs(cur, table):
    cur.execute("select column_name, data_type, is_identity from information_schema.columns where table_schema = 'public' and table_name = %s ", (table,))
    return [ row for row in cur ]

enum_tmpl = """
package jp.chang.myclinic.dbxfer.table;

public interface {tablename}Enum {{
{members}
}}
"""

table_tmpl = """
package jp.chang.myclinic.dbxfer.table;

import static jp.chang.myclinic.dbxfer.table.{tablename}Enum.*;

public class {tablename} extends Table<{tablename}Enum> {{
    
    public {tablename}(){{
        super("{table}");
{colspecs}    }}

    @Override
    public {tablename}Enum[] listColumnEnums(){{
        return {tablename}Enum.values();
    }}

}}

"""

def camel(s):
    return "".join([a.title() for a in s.split("_")])


def new_column(name, data_type, is_identity):
    if data_type in ("text", "character varying"):
        return 'new StringColumn("%s")' % name
    elif data_type in ("integer", "smallint"):
        if is_identity:
            return 'new SerialColumn("%s")' % name
        else:
            return 'new IntegerColumn("%s")' % name
    else:
        print("unknown type: %s" % data_type)
        return 'new UnknownColumn("%s")' % name

def colstmt(colspec):
    (name, data_type, is_identity) = colspec
    return """        setColumn(%s, %s);\n""" % (name.upper(), new_column(name, data_type, is_identity))


if __name__ == "__main__":
    what = sys.argv[1]
    if( what == "enum" ):
        conn = connect()
        cur = conn.cursor()
        for table in get_table_names(cur):
            cols = get_column_names(cur, table)
            members = ",\n".join([ "    " + col.upper() for col in cols])
            tmpl = enum_tmpl.format(tablename=camel(table), members=members)
            print(tmpl)
        cur.close()
        conn.close()
    elif( what == "table" ):
        conn = connect()
        cur = conn.cursor()
        for table in get_table_names(cur):
            colspecs = get_column_specs(cur, table)
            var_colspecs = "".join([ colstmt(cs) for cs in colspecs ])
            tmpl = table_tmpl.format(tablename=camel(table), table=table, colspecs=var_colspecs)
            print(tmpl)
        cur.close()
        conn.close()






