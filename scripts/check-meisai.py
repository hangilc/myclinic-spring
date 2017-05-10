import requests
import argparse

parser = argparse.ArgumentParser(description="Check new meisai calc")
parser.add_argument("--upper", type=int, action="store", dest="upper_visit_id")
#parser.add_argument("--lower", type=int, action="store", dest="lower_visit_id")
opt = parser.parse_args()
upper_visit_id = opt.upper_visit_id

new_server = "http://localhost:8080/json"
old_server = "http://localhost:9000/service";

r = requests.get(new_server + "/list-visit-ids")
for visit_id in r.json():
	if (not (upper_visit_id is None)) and visit_id > upper_visit_id:
		continue
	if visit_id % 1000 == 0:
		print visit_id
	new_meisai_resp = requests.get(new_server + "/get-visit-meisai?visit-id=" + str(visit_id))
	old_meisai_resp = requests.get(old_server + "?_q=calc_meisai&visit_id=" + str(visit_id))
	if new_meisai_resp.status_code != 200:
		print "cannot get meisai for visit_id: " + str(visit_id)
		continue
	if old_meisai_resp.status_code != 200:
		print "cannot get meisai for (old) visit_id: " + str(visit_id)
		continue
	new_meisai = new_meisai_resp.json()
	old_meisai = old_meisai_resp.json()
	if new_meisai["totalTen"] is None or new_meisai["totalTen"] != old_meisai["totalTen"]:
		print "totalTen differs visit_id: " + str(visit_id)
		print "new_meisai: " + str(new_meisai["totalTen"])
		print "old_meisai: " + str(old_meisai["totalTen"])
		exit(1)
	if new_meisai["futanWari"] is None or new_meisai["futanWari"] != old_meisai["futanWari"]:
		print "futanWari differs visit_id: " + str(visit_id)
		print "new_meisai: " + str(new_meisai["futanWari"])
		print "old_meisai: " + str(old_meisai["futanWari"])
		exit(1)
	if new_meisai["charge"] is None or new_meisai["charge"] != old_meisai["charge"]:
		print "charge differs visit_id: " + str(visit_id)
		print "new_meisai: " + str(new_meisai["charge"])
		print "old_meisai: " + str(old_meisai["charge"])
		exit(1)

