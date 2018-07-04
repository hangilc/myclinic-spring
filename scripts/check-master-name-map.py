# usage: python check-master-name-map.py year month day
import sys
import os
import mysql.connector

year = int(sys.argv[1])
month = int(sys.argv[2])
day = int(sys.argv[3])

at = "{}-{:02d}-{:02d}".format(year, month, day)

conn = mysql.connector.connect(
	host=os.environ["MYCLINIC_DB_HOST"],
	user=os.environ["MYCLINIC_DB_USER"], 
    password=os.environ["MYCLINIC_DB_PASS"], database="myclinic", charset="utf8")
cursor = conn.cursor()

master_map_file = "./config/master-name.txt"
code_map_file = "./config/master-map.txt"

iyakuhin_code_map = {}
shinryou_code_map = {}

code_map_map = { 'Y': iyakuhin_code_map, 'S' : shinryou_code_map }

def read_code_map(file):
    with open(file, encoding="utf-8") as f:
        for line in f:
            parts = line.strip().split(",")
            if len(parts) < 4:
                continue
            kind = parts[0]
            from_code = int(parts[1])
            date = parts[2]
            to_code = int(parts[3].split(" ")[0])
            if kind not in ['Y', 'S']: 
                continue
            if at >= date:
                code_map_map[kind][from_code] = to_code    

def resolve_with_code_map(code, code_map):
    while code in code_map:
        code = code_map[code]
    return code   

def find_shinryou(shinryoucode):
    cursor.execute("""
        select shinryoucode from shinryoukoui_master_arch 
        where shinryoucode = %s
        and valid_from <= %s 
        and ( valid_upto = '0000-00-00' or valid_upto >= %s )
        """, (shinryoucode, at, at))
    return cursor.fetchall()

def handle_shinryou(name, code):
    code = resolve_with_code_map(code, shinryou_code_map)
    if code == 0:
        return
    rows = find_shinryou(code)
    if len(rows) == 0:
        print("Missing {} ({})".format(name, code))
    elif len(rows) != 1:
        print("Duplicates {} ({})".format(name, code))

read_code_map(code_map_file)

with open(master_map_file, encoding="utf-8") as f:
    for line in f:
        parts = line.strip().split(",")
        if len(parts) < 3:
            continue
        kind = parts[0]
        if kind == 's':
            handle_shinryou(parts[1], int(parts[2]))
            
cursor.close()
conn.close()

