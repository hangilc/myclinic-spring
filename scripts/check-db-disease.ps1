mysql -u hangil -p myclinic -e "select d.* from disease d, shoubyoumei_master_arch m where d.shoubyoumeicode = m.shoubyoumeicode and d.start_date < m.valid_from"
