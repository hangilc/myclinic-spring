Title: MySQL の 5.6 から 5.7 へのアップグレード
Date: 2018-06-27 08:34
Category: development, mysql

MySQL を version 5.6 から 5.7 へアップグレードした記録。
Chocolatey では、MySQL 5.7 がインストールされるので、新規インストールの便宜のために、現在使用している MySQL 5.6 から MySQL 5.7 にアップグレードした。

まず、現在のデータベースの内容の保存。 以下、**cmd.exe** で実行しているものとする。 

```
> start cmd.exe
```

```
> mysqldump -u (USER) -p --databases myclinic intraclinic --single-transaction --flush-logs --master-data=2 --add-drop-table >dump56.sql 
```

「プログラムと機能」で、MySQL 5.6 をアンインストールする。

MySQL Installer で MySQL 5.7 をインストール。

データベースの内容の読み込み。これは、cmd.exe で実行する！

```
> mysql -u (USER) -p <dump56.sql 
```

データベースの upgrade を確認する。

```
> mysql_upgrade -u root -p
```

ユーザーの追加。

```
> mysql -u root -p
mysql> grant all on myclinic.* to staff identified by '...';
mysql> grant all on intraclinic.* to staff;
```


