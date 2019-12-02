create user repl@'%' identified by 'replpass';
grant replication slave on *.* to repl@'%';
	