import csv
import os
import pymysql

class YakuzaiEntry:
	def __init__(self, kubun, yakkacode, kikaku, hinmei, is_kouhatsu, has_kouhatsu):
		self.kubun = kubun
		self.yakkacode = yakkacode
		self.kikaku = kikaku
		self.hinmei = hinmei
		self.is_kouhatsu = is_kouhatsu
		self.has_kouhatsu = has_kouhatsu

def parse_row(row):
	return YakuzaiEntry(row[0], row[1], row[3], row[7], 
		row[9] == "後発品", row[11] == "○")

def load_yakuzai_file(path, storeDict):
	f = open(path, encoding="cp932")
	reader = csv.reader(f)
	reader.__next__()
	for row in reader:
		entry = parse_row(row)
		storeDict[entry.yakkacode] = entry
	f.close()

def list_drugs(cursor, date_from, date_upto):
	cursor.execute("select d.*, m.* from visit_drug d, visit v, iyakuhin_master_arch m " +
		" where d.visit_id = v.visit_id " +
		" and date(v.v_datetime) >= %s and date(v.v_datetime) <= %s " +
		" and m.iyakuhincode = d.d_iyakuhincode  " +
		" and m.valid_from <= date(v.v_datetime) " +
		" and (m.valid_upto = '0000-00-00' or m.valid_upto >= date(v.v_datetime)) ",
		(date_from, date_upto))
	return cursor.fetchall()

def calc_term(cursor, date_from, date_upto, yakuzaiDict):
	drugs = list_drugs(cursor, date_from, date_upto)
	c_kouhatsu = 0
	c_senpatsu = 0
	c_total = 0
	for drug in drugs:
		if drug["d_category"] == 2:
			days = 1
		else:
			days =  drug["d_days"]
		count = float(drug["d_amount"]) * int(drug["d_days"])
		if not (drug["yakkacode"] in yakuzaiDict):
			#print(drug["name"] + " -> kouhatsu")
			c_kouhatsu += count
			c_total += count
		else:
			entry = yakuzaiDict[drug["yakkacode"]]
			if entry.is_kouhatsu:
				c_kouhatsu += count
			elif entry.has_kouhatsu:
				c_senpatsu += count
			c_total += count
	return (c_total, c_senpatsu + c_kouhatsu, c_kouhatsu)

# def print_term(term):
# 	print((term[0], term[1], term[2], term[2]/term[0], term[2]/term[1]))

def print_term(term):
	print((term[0], term[1], term[2], term[1]/term[0], term[2]/term[1]))

if __name__ == "__main__":
	yakuzaiDict = {}
	year = "2018"
	for p in ["work/tp20180314-01_1.csv", "work/tp20180401-01_2.csv", "work/tp20180401-01_3.csv"]:
		load_yakuzai_file(p, yakuzaiDict)
	db_user = os.getenv("MYCLINIC_DB_USER")
	db_pass = os.getenv("MYCLINIC_DB_PASS")
	conn = pymysql.connect(user=db_user, passwd=db_pass, db="myclinic", charset="utf8")
	cursor = conn.cursor(pymysql.cursors.DictCursor)
	term1 = calc_term(cursor, year + "-01-01", year + "-01-31", yakuzaiDict)
	term2 = calc_term(cursor, year + "-02-01", year + "-02-29", yakuzaiDict)
	term3 = calc_term(cursor, year + "-03-01", year + "-03-31", yakuzaiDict)
	conn.close()
	print_term(term1)
	print_term(term2)
	print_term(term3)
	print_term((term1[0]+term2[0]+term3[0], term1[1]+term2[1]+term3[1], term1[2]+term2[2]+term3[2]))

	
