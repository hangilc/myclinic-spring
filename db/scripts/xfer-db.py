import db_postgresql
import db_mysql

def zero_to_null(i):
    if i == 0:
        return None
    else:
        return i

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
    },
    "convert_maps": {
        "mysql_to_postgresql": {
            "visit": {
                "shahokokuho_id": zero_to_null,
                "roujin_id": zero_to_null,
                "koukikourei_id": zero_to_null,
                "kouhi_1_id": zero_to_null,
                "kouhi_2_id": zero_to_null,
                "kouhi_3_id": zero_to_null
            }
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
            if( k in src_names ):
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

def find_identity_column_name(colspec_list):
    for i, c in enumerate(colspec_list):
        if c["is_identity"]:
            return c["column_name"]
    return None

def xfer_table(src_table, src_db, dst_table, dst_db, hint, converts):
    src_cols = src_table["columns"]
    dst_cols = dst_table["columns"]
    print("==", "xfer", src_table["table_name"], "==")
    col_map = match_columns(src_cols, dst_cols, hint)
    src_colnames = list(col_map.keys())
    select_sql = "select %s from %s" % (", ".join(src_colnames), src_table["table_name"])
    dst_colnames = list(col_map.values())
    insert_sql = "insert into %s (%s) values (%s)" % ( \
        dst_table["table_name"], \
        ", ".join(dst_colnames), \
        ", ".join(["%s" for c in dst_colnames]) )
    cvt_procs = [ (src_colnames.index(c), f) for c, f in converts.items() ]
    identity_column_index = -1
    dst_identity_column_name = find_identity_column_name(dst_cols.values())
    if dst_identity_column_name in dst_colnames:
        identity_column_index = dst_colnames.index(dst_identity_column_name)
    count = 0
    max_id = 0
    def proc(r):
        nonlocal count
        nonlocal max_id
        if len(cvt_procs) > 0:
            tmp = list(r)
            for i, f in cvt_procs:
                tmp[i] = f(tmp[i])
            r = tuple(tmp)
        dst_db.execute_no_result(insert_sql, r)
        if identity_column_index >= 0:
            max_id = r[identity_column_index]
        count += 1
        if count % 1000 == 0:
            print(count)
    src_db.execute_proc(select_sql, proc)
    if max_id > 0:
        dst_db.set_next_serial_value(dst_table["table_name"], dst_identity_column_name, max_id + 1)
    
def xfer_tables(table_pair_list, src_db, dst_db, hint, converts):
    for src_table, dst_table in table_pair_list:
        xfer_table(src_table, src_db, dst_table, dst_db, 
            hint.get(src_table["table_name"], dict()),
            converts.get(src_table["table_name"], dict()))

def xfer(src_arg, dst_arg):
    src_db_info = src_arg["db"].get_db_info()
    dst_db_info = dst_arg["db"].get_db_info()
    ordered_tables = order_tables(dst_db_info)
    dst_to_src_table_map = create_matches(
        sorted(dst_arg["db"].get_table_names()),
        sorted(src_arg["db"].get_table_names()),
        hints["table_maps"]["%s_to_%s" % (dst_arg["kind"], src_arg["kind"])]
        )
    table_pair_list = [ 
        (src_db_info[dst_to_src_table_map[table]], dst_db_info[table])
        for table in ordered_tables ]
    xfer_tables(table_pair_list, src_arg["db"], dst_arg["db"],
        hints["column_maps"]["%s_to_%s" % (src_arg["kind"], dst_arg["kind"])],
        hints["convert_maps"]["%s_to_%s" % (src_arg["kind"], dst_arg["kind"])])


def parse_arg(arg):
    parts = arg.split(":")
    kind = parts.pop(0)
    database = parts.pop() if parts else "myclinic"
    return { "kind": kind, "database": database }

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 3:
        print("Usage: xfer-db srckind[:database] dstkind[:database]")
        exit(1)
    (src_input, dst_input) = sys.argv[1:3]
    src_arg = parse_arg(src_input)
    dst_arg = parse_arg(dst_input)
    for arg in (src_arg, dst_arg):
        if arg["kind"] == "mysql":
            arg["db"] = db_mysql.DbMySQL(arg["database"])
        elif arg["kind"] == "postgresql":
            arg["db"] = db_postgresql.DbPostgreSQL(arg["database"])
        else:
            raise RuntimeError("Unknown kind: %s" % (arg["kind"],))
    xfer(src_arg, dst_arg)


