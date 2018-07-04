import mysql.connector
import os;
import sys;
from prettytable import PrettyTable
from prettytable import from_db_cursor

text = sys.argv[1]
conn = mysql.connector.connect(
	host=os.environ["MYCLINIC_DB_HOST"],
	user=os.environ["MYCLINIC_DB_USER"], 
    password=os.environ["MYCLINIC_DB_PASS"], database="myclinic", charset="utf8")
cursor = conn.cursor()
cursor.execute("""select shinryoucode,valid_from,valid_upto,name 
	from shinryoukoui_master_arch where name like %s
	order by name, valid_from, shinryoucode
	""", 
	(f"%{text}%",))
for(shinryoucode, valid_from, valid_upto, name) in cursor:
	print("{}, {}, {}, {}".format(shinryoucode, valid_from, valid_upto, name))
cursor.close()
conn.close()




