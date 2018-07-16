Title: 新規インストール
Date: 2018-06-25 15:58
Category: development
Status: draft

myclinic-spring を新しいＰＣにインストールしてみる。今回使ったＰＣは Lenovo ノートパソコン ideapad 320 (Core i5, 8GB Memory, 256GB SSD) で、価格は 76,400 円だった。

不要なソフトをアンインストールして、Windows の更新プログラムをインストール（２時間かかった）。

JDK10 のインストール

http://www.oracle.com/technetwork/java/javase/downloads/index.html

環境変数 JAVA_HOME, PATH を設定する。

Maven のインストール

https://maven.apache.org/download.cgi

環境変数 PATH を設定。

Python 3 をインストール

https://www.python.org/downloads/

環境変数 PATH を設定。

Git をインストール。

https://git-scm.com/downloads

MySQL 5.7 のインストール

```shell
> mysql -u root -p

mysql> grant all on myclinic.* to {USERNAME} identified by '{PASSWORD}';
```

myclinic-spring の clone

```shell
> git clone https://github.com/hangilc/myclinic-spring.git
> cd myclinic-spring
> xcopy config-example config /i
> python -m venv my-pyenv
> py-myenv\scripts\activate
> pip install -r scripts\pip-freeze.txt
> mvn install
```

master files のダウンロード

```shell
> scripts\download-all-masters.bat
```

環境変数の設定。

```
MYCLINIC_DB_HOST -- database host の名前 (localhost)
MYCLINIC_DB_USER -- name of db user
MYCLINIC_DB_PASS -- db password
```

database の作成

```shell
> mysql -u root -p

mysql> create database myclinic;
mysql> exit;

> cd myclinic-spring
> mysql -u hangil -p myclinic <server\migrations\schema.sql
```

