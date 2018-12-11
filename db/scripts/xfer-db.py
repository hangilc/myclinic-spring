import mysql.connector
import psycopg2
import os


def extract_table_names(tables):
	return [ t["tablename"] for t in tables ]

table_name_map = {
	"iyakuhin_master": "iyakuhin_master_arch",
	"shinryou_master": "shinryoukoui_master_arch",
	"kizai_master": "tokuteikizai_master_arch",
	"byoumei_master": "shoubyoumei_master_arch",
	"shahokokuho": "hoken_shahokokuho",
	"roujin": "hoken_roujin",
	"koukikourei": "hoken_koukikourei",
	"gazou_label": "visit_gazou_label",
	"text": "visit_text",
	"drug": "visit_drug",
	"shinryou": "visit_shinryou",
	"conduct": "visit_conduct",
	"conduct_drug": "visit_conduct_drug",
	"conduct_shinryou": "visit_conduct_shinryou",
	"conduct_kizai": "visit_conduct_kizai",
	"charge": "visit_charge",
	"payment": "visit_payment"
}

def compare_table_names(postgresql_table_names, mysql_table_names):
	for t in postgresql_table_names:
		if t in mysql_table_names:
			table_name_map[t] = t


xfer_tables = [
	"iyakuhin_master"
]

if __name__ == "__main__":
	(mysql_spec, postgresql_spec) = read_db_specs()
	mysql_table_names = extract_table_names(mysql_spec["tables"])
	postgresql_table_names = extract_table_names(postgresql_spec["tables"])
