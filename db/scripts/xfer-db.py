import db_postgresql
import db_mysql

hints = {
    "skip_mysql_tables": [ "stock_drug", "shinryou_view" ],
    "postgresql_to_mysql_map": {
        "shinryou_master": "shinryoukoui_master_arch",
        "drug": "visit_drug",
        "shinryou": "visit_shinryou"
    }
}

def find_extender(str, list):
    for s in list:
        if str in s:
            return s
    return None

def create_table_map(src_table_names, dst_table_names):
    map = {}
    src_list = src_table_names[:]
    dst_list = dst_table_names[:]
    for src, dst in hints["postgresql_to_mysql_map"].items():
        map[src] = dst
        src_list.remove(src)
        dst_list.remove(dst)
    for src in src_list[:]:
        if src in dst_list:
            map[src] = src
            src_list.remove(src)
            dst_list.remove(src)
    for src in src_list[:]:
        dst = find_extender(src, dst_list)
        if dst:
            map[src] = dst
            src_list.remove(src)
            dst_list.remove(dst)
    for dst in dst_list[:]:
        src = find_extender(dst, src_list)
        if src:
            map[src] = dst
            src_list.remove(src)
            dst_list.remove(dst)
    return (map, src_list, dst_list)

def order_tables(db_info):
    result = []
    branches = []
    for table in db_info:
        if len(table["referring"]) == 0:
            result.append(table["table_name"])
        else:
            branches.append(table)
    handled = len(result)
    while( handled > 0 ):
        handled = 0
        result_set = set(result)
        adding = []
        for branch in branches[:]:
            ref_set = set(branch["referring"])
            if result_set.issuperset(ref_set):
                adding.append(branch["table_name"])
                branches.remove(branch)
                handled += 1
        result.extend(adding)
    if len(branches) > 0:
        print(branches)
        raise RuntimeError("Cannot order tables")
    return result


if __name__ == "__main__":
    mysql_info = db_mysql.get_db_info()
    postgresql_info = db_postgresql.get_db_info()
    mysql_table_names = [ a["table_name"] for a in mysql_info ]
    postgresql_table_names = [ a["table_name"] for a in postgresql_info ]
    for skip in hints["skip_mysql_tables"]:
        mysql_table_names.remove(skip)
    (table_map, s, d) = create_table_map(postgresql_table_names, mysql_table_names)
    if s != [] or d != []:
        raise RuntimeError("Cannot map tables")
    print("== table map ==")
    for k, v in table_map.items():
        print(k, "->", v)
    ordered_tables = order_tables(postgresql_info)
    print("== table order ==")
    for table in ordered_tables:
        print(table)
    print("== %d of %d tables ordered ==" % (len(ordered_tables), len(postgresql_table_names)))
    if len(postgresql_table_names) != len(ordered_tables):
        raise RuntimeError("Cannot order tables")

