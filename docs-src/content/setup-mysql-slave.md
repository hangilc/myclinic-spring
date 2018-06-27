Title: MySQL slave の設定
Date: 2018-06-27 14:28
Category: development, mysql

MySQL slave の設定。

MySQL master でバックアップファイルを作成。

my.cnf の設定 (slave で)

```
[mysqld]
server-id=2
```

ユーザー権限の設定 (slave で)

```
mysql> grant all on myclinic.* to (USER) identified by '...';
mysql> grant all on intraclinic.* to (USER)
```

(master で)

```
> mysqldump -u (USER) -p --databases myclinic intraclinic --single-transaction --flush-logs --master-data=2 --add-drop-table >dump.sql 
> mysql -h (SLAVE-HOST) -u (USER) -p <dump.sql
> grep 'CHANGE MASTER TO' dump.sql
```

(salve で)

```
> mysql -u root -p
mysql> stop slave;
mysql> change master to master_host='...', master_user='...', master_password='...', master_log_file='...', master_log_pos=...;
mysql> start slave;
mysql> show slave status\G
```




