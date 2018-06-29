Title: MySQL master の設定
Date: 2018-06-27 08:34
Category: development, mysql

MySQL master の設定。

my.ini の編集。Windows OS で、インストーラーを使ってインストールしているので、my.ini は C:\ProgramData\MySQL\MySQL Server 5.6 にある。

```
[mysqld]
log-bin=mysql-bin
server-id=1
```

server-id には、そのサーバーの ID を設定（MySQL server ごとに個別の数値を設定する）。

log-bin を設定することで、binary log を保存するようになり、マスターとして機能できるようになる。

ユーザー権限の付与。

```
mysql> grant all on myclinic.* to (USER);
mysql> grant all on intraclinic.* to (USER);
mysql> grant replication slave on *.* to (USER);
mysql> grant replication client on *.* to (USER);
```

