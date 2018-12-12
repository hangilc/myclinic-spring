import db_postgresql
import db_mysql

hints = {
    "table_maps": {
        "mysql_to_postgresql": {
            "stock_drug": None,
            "shinryou_view": None,
            "shinryoukoui_master_arch": "shinryou_master",
            "visit_drug": "drug",
            "visit_shinryou": "shinryou",
            "visit_conduct": "conduct"
        },
        "postgresql_to_mysql": {

        }
    },
    "column_maps": {
        "mysql_to_postgresql": {
            "iyakuhin_master_arch": {
                "yakkacode": None
            },
            "shinryoukoui_master_arch":{
                "oushinkubun": None
            },
            "hotline":{
                "m_datetime": "posted_at"
            },
            "patient":{
                "birth_day": "birthday"
            },
            "pharma_drug": {
                "sideeffect": "side_effect"
            },
            "visit": {
                "v_datetime": "visited_at"
            },
            "visit_conduct_drug": {
                "id": "conduct_drug_id"
            },
            "visit_conduct_shinryou": {
                "id": "conduct_shinryou_id"
            },
            "visit_conduct_kizai": {
                "id": "conduct_kizai_id"
            }
        },
        "postgresql_to_mysql": {

        }
    }
}

hints["table_maps"]["postgresql_to_mysql"].update(
    {v: k for k, v in hints["table_maps"]["mysql_to_postgresql"].items() if v != None })

hints["column_maps"]["postgresql_to_mysql"].update(
    {hints["table_maps"]["mysql_to_postgresql"].get(mysql_table, mysql_table):
        {v: k for k, v in colmap.items() if v != None }
        for mysql_table, colmap in hints["column_maps"]["mysql_to_postgresql"].items()})

def find_extender(str, list):
    for s in list:
        if str in s:
            return s
    return None

def create_matches(src_names, dst_names, hint_map={}):
    src_names = src_names[:]
    dst_names = dst_names[:]
    map = {}
    for k, v in hint_map.items():
        if v == None:
            src_names.remove(k)
        else:
            map[k] = v
            src_names.remove(k)
            dst_names.remove(v)
    for k in src_names[:]:
        if k in dst_names[:]:
            map[k] = k
            src_names.remove(k)
            dst_names.remove(k)
    handled = 1
    while handled > 0:
        handled = 0
        for s in src_names[:]:
            for t in dst_names[:]:
                if (s in t) or (t in s) :
                    map[s] = t
                    src_names.remove(s)
                    dst_names.remove(t)
                    handled += 1
    if len(src_names) > 0:
        print(src_names, dst_names)
        raise RuntimeError("Cannot match names.")
    return map

def order_tables(db_info):
    result = []
    branches = []
    for table in db_info.values():
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

def match_columns(src_cols, dst_cols, hint):
    return create_matches(sorted(src_cols.keys()), sorted(dst_cols.keys()), hint)

def xfer_table(src_table, src_cur, dst_table, dst_cur, hint):
    src_cols = src_table["columns"]
    dst_cols = dst_table["columns"]
    print("==", "xfer", src_table["table_name"], "==")
    col_map = match_columns(src_cols, dst_cols, hint)
    src_cols = ", ".join(col_map.keys())
    select_sql = "select %s from %s" % (src_cols, src_table["table_name"])
    dst_cols = ", ".join(col_map.values())
    dst_para = ", ".join(["?" for c in col_map.values()])
    insert_sql = "insert into %s (%s) values (%s)" % (dst_table["table_name"], dst_cols, dst_para)

def xfer_tables(table_pair_list, src_cur, dst_cur, hint):
    for src_table, dst_table in table_pair_list:
        xfer_table(src_table, src_cur, dst_table, dst_cur, hint.get(src_table["table_name"], dict()))

if __name__ == "__main__":
    mysql_info = db_mysql.get_db_info()
    postgresql_info = db_postgresql.get_db_info()
    mysql_table_names = [ a["table_name"] for a in mysql_info.values() ]
    postgresql_table_names = [ a["table_name"] for a in postgresql_info.values() ]
    mysql_to_postgresql_table_map = create_matches(
        sorted(mysql_table_names), sorted(postgresql_table_names), 
        hints["table_maps"]["mysql_to_postgresql"])
    postgresql_to_mysql_table_map = {
        v: k for k, v in mysql_to_postgresql_table_map.items() if v != None
    }
    print("== table map ==")
    for k, v in mysql_to_postgresql_table_map.items():
        print(k, "->", v)
    ordered_tables = order_tables(postgresql_info)
    print("== table order ==")
    for table in ordered_tables:
        print(table)
    print("== %d of %d tables ordered ==" % (len(ordered_tables), len(postgresql_table_names)))
    if len(postgresql_table_names) != len(ordered_tables):
        raise RuntimeError("Cannot order tables")
    table_pair_list = [ 
        (mysql_info[postgresql_to_mysql_table_map[table]], postgresql_info[table])
        for table in ordered_tables ]
    xfer_tables(table_pair_list, db_mysql.get_cur(), db_postgresql.get_cur(),
        hints["column_maps"]["mysql_to_postgresql"])
