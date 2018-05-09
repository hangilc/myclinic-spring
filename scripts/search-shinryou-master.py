import MySQLdb
import os;
import sys;
from prettytable import PrettyTable
from prettytable import from_db_cursor

text = sys.argv[1]
conn = MySQLdb.connect(user=os.environ["MYCLINIC_DB_USER"], 
	passwd=os.environ["MYCLINIC_DB_PASS"], db="myclinic", charset="utf8")
cursor = conn.cursor()
cursor.execute("""select shinryoucode,valid_from,valid_upto,name 
	from shinryoukoui_master_arch where name like %s
	order by name, valid_from, shinryoucode
	""", 
	(f"%{text}%",))
t = from_db_cursor(cursor)
print(t)




