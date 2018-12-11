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


if __name__ == "__main__":
	mysql_table_names = db_mysql.get_table_names()
	postgresql_table_names = db_postgresql.get_table_names()
	for skip in hints["skip_mysql_tables"]:
		mysql_table_names.remove(skip)
	(table_map, s, d) = create_table_map(postgresql_table_names, mysql_table_names)
	if s != [] or d != []:
		raise RuntimeError("Cannot map tables")
	print("== table map ==")
	for k, v in table_map.items():
		print(k, "->", v)

