import db_postgresql
import db_mysql
import json


def zero_to_null(i):
    if i == 0:
        return None
    else:
        return i


def null_to_zero(v):
    if v is None:
        return 0
    else:
        return v


def null_to_old_sqldate(v):
    if v is None:
        return "0000-00-00"
    else:
        return v


def dict_to_json(d):
    return json.dumps(d)


def kizai_kingaku_to_str(k):
    return str(int(k))

hints = {
    "table_maps": {
        "mysql_to_postgresql": {
            "stock_drug": None,
            "shinryou_view": None,
            "shinryoukoui_master_arch": "shinryou_master",
            "visit_drug": "drug",
            "visit_shinryou": "shinryou",
            "visit_conduct": "conduct",
            "visit_conduct_shinryou": "conduct_shinryou",
            "visit_conduct_drug": "conduct_drug",
            "visit_conduct_kizai": "conduct_kizai"
        },
        "postgresql_to_mysql": {

        }
    },
    "column_maps": {
        "mysql_to_postgresql": {
            "iyakuhin_master_arch": {
                "yakkacode": None
            },
            "shinryoukoui_master_arch": {
                "oushinkubun": None
            },
            "hotline": {
                "m_datetime": "posted_at"
            },
            "patient": {
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
        },
        "postgresql_to_mysql": {
            "visit": {
                "shahokokuho_id": null_to_zero,
                "roujin_id": null_to_zero,
                "koukikourei_id": null_to_zero,
                "kouhi_1_id": null_to_zero,
                "kouhi_2_id": null_to_zero,
                "kouhi_3_id": null_to_zero
            },
            "disease": {
                "end_date": null_to_old_sqldate
            },
            "koukikourei": {
                "valid_upto": null_to_old_sqldate
            },
            "shahokokuho": {
                "valid_upto": null_to_old_sqldate
            },
            "roujin": {
                "valid_upto": null_to_old_sqldate
            },
            "kouhi": {
                "valid_upto": null_to_old_sqldate
            },
            "iyakuhin_master": {
                "valid_upto": null_to_old_sqldate
            },
            "shinryou_master": {
                "valid_upto": null_to_old_sqldate
            },
            "kizai_master": {
                "kingaku": kizai_kingaku_to_str,
                "valid_upto": null_to_old_sqldate
            },
            "practice_log": {
                "body": dict_to_json
            },
            "byoumei_master": {
                "valid_upto": null_to_old_sqldate
            }
        }
    },
    "destination_setups":{
        "postgresql_to_mysql": [
            "alter table shinryoukoui_master_arch alter oushinkubun set default '0'",
            "alter table iyakuhin_master_arch alter yakkacode set default '0'"
        ]
    }
}

hints["table_maps"]["postgresql_to_mysql"].update(
    {v: k for k, v in hints["table_maps"]["mysql_to_postgresql"].items() if v is not None})

hints["column_maps"]["postgresql_to_mysql"].update(
    {hints["table_maps"]["mysql_to_postgresql"].get(mysql_table, mysql_table):
        {v: k for k, v in colmap.items() if v is not None}
     for mysql_table, colmap in hints["column_maps"]["mysql_to_postgresql"].items()})

def find_extender(needle, haystack):
    for s in haystack:
        if needle in s:
            return s
    return None


def create_matches(src_names, dst_names, hint_map={}):
    src_names = src_names[:]
    dst_names = dst_names[:]
    result_map = {}
    for k, v in hint_map.items():
        if v is None:
            if k in src_names:
                src_names.remove(k)
        else:
            result_map[k] = v
            src_names.remove(k)
            dst_names.remove(v)
    for k in src_names[:]:
        if k in dst_names[:]:
            result_map[k] = k
            src_names.remove(k)
            dst_names.remove(k)
    handled = 1
    while handled > 0:
        handled = 0
        for s in src_names[:]:
            for t in dst_names[:]:
                if (s in t) or (t in s):
                    result_map[s] = t
                    src_names.remove(s)
                    dst_names.remove(t)
                    handled += 1
    if len(src_names) > 0:
        print(src_names, dst_names)
        raise RuntimeError("Cannot match names.")
    return result_map


def order_tables(db_info):
    result = []
    branches = []
    for table in db_info.values():
        if len(table["referring"]) == 0:
            result.append(table["table_name"])
        else:
            branches.append(table)
    handled = len(result)
    while handled > 0:
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
    cvt_procs = [(src_colnames.index(c), f) for c, f in converts.items()]
    identity_column_index = -1
    dst_identity_column_name = find_identity_column_name(dst_cols.values())
    if dst_identity_column_name in dst_colnames:
        identity_column_index = dst_colnames.index(dst_identity_column_name)
    rows = src_db.execute(select_sql)
    values_list = []
    count = 0
    max_id = 0
    for r in rows:
        if len(cvt_procs) > 0:
            tmp = list(r)
            for i, f in cvt_procs:
                tmp[i] = f(tmp[i])
            values_list.append(tmp)
        else:
            values_list.append(r)
        if identity_column_index >= 0:
            max_id = r[identity_column_index]
    for i in range(0, len(values_list), 100):
        values = values_list[i:(i+100)]
        dst_db.batch_insert(dst_table["table_name"], dst_colnames, values)
        count += len(values)
        if count % 1000 == 0:
            print(count)
    if max_id > 0:
        dst_db.set_next_serial_value(dst_table["table_name"], dst_identity_column_name, max_id + 1)

# def xfer_table(src_table, src_db, dst_table, dst_db, hint, converts):
#     src_cols = src_table["columns"]
#     dst_cols = dst_table["columns"]
#     print("==", "xfer", src_table["table_name"], "==")
#     col_map = match_columns(src_cols, dst_cols, hint)
#     src_colnames = list(col_map.keys())
#     select_sql = "select %s from %s" % (", ".join(src_colnames), src_table["table_name"])
#     dst_colnames = list(col_map.values())
#     insert_sql = "insert into %s (%s) values (%s)" % (
#         dst_table["table_name"],
#         ", ".join(dst_colnames),
#         ", ".join(["%s"] * len(dst_colnames))
#     )
#     cvt_procs = [(src_colnames.index(c), f) for c, f in converts.items()]
#     identity_column_index = -1
#     dst_identity_column_name = find_identity_column_name(dst_cols.values())
#     if dst_identity_column_name in dst_colnames:
#         identity_column_index = dst_colnames.index(dst_identity_column_name)
#     count = 0
#     max_id = 0

#     def proc(r):
#         nonlocal count
#         nonlocal max_id
#         if len(cvt_procs) > 0:
#             tmp = list(r)
#             for i, f in cvt_procs:
#                 tmp[i] = f(tmp[i])
#             r = tuple(tmp)
#         dst_db.execute_no_result(insert_sql, r)
#         if identity_column_index >= 0:
#             max_id = r[identity_column_index]
#         count += 1
#         if count % 1000 == 0:
#             print(count)

#     src_db.execute_proc(select_sql, proc)
#     if max_id > 0:
#         dst_db.set_next_serial_value(dst_table["table_name"], dst_identity_column_name, max_id + 1)


def xfer_tables(table_pair_list, src_db, dst_db, hint, converts):
    for src_table, dst_table in table_pair_list:
        xfer_table(src_table, src_db, dst_table, dst_db,
                   hint.get(src_table["table_name"], dict()),
                   converts.get(src_table["table_name"], dict()))


def xfer(src_arg, dst_arg):
    src_db_info = src_arg["db"].get_db_info()
    dst_db_info = dst_arg["db"].get_db_info()
    dst_to_src_table_map = create_matches(
        sorted(dst_arg["db"].get_table_names()),
        sorted(src_arg["db"].get_table_names()),
        hints["table_maps"]["%s_to_%s" % (dst_arg["kind"], src_arg["kind"])]
    )
    ordered_tables = order_tables(dst_db_info)
    ordered_tables = [t for t in ordered_tables if t in dst_to_src_table_map]

    # start_idx = ordered_tables.index("visit_conduct_kizai")
    # ordered_tables = ordered_tables[start_idx:]

    # ordered_tables = ["iyakuhin_master"]

    xfer_key = "%s_to_%s" % (src_arg["kind"], dst_arg["kind"])
    for sql in hints["destination_setups"].get(xfer_key, []):
        dst_arg["db"].execute_no_result(sql)
    table_pair_list = [
        (src_db_info[dst_to_src_table_map[table]], dst_db_info[table])
        for table in ordered_tables]
    xfer_tables(table_pair_list, src_arg["db"], dst_arg["db"],
                hints["column_maps"][xfer_key],
                hints["convert_maps"][xfer_key])


def parse_arg(argument):
    parts = argument.split(":")
    kind = parts.pop(0)
    database = parts.pop() if parts else "myclinic"
    return {"kind": kind, "database": database}


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
