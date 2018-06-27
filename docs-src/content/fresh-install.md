Title: 新規インストール
Date: 2018-06-25 15:58
Category: development
Status: draft

myclinic-spring を新しいＰＣにインストールしてみる。今回使ったＰＣは Lenovo ノートパソコン ideapad 320 (Core i5, 8GB Memory, 256GB SSD) で、価格は 76,400 円だった。

不要なソフトをアンインストールして、Windows の更新プログラムをインストール（２時間かかった）。

Chocholatey をインストール。PowerShell を管理者として起動。プログラムを shell から起動できるようにする。

```shell
> Set-ExecutionPolicy -Force RemoteSigned
```

```shell
>  iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
```

JDK10 のインストール

```shell
> choco install -y jdk10
```

GIT のインストール

```shell
> choco install -y git
```

Maven のインストール

```shell
> choco install -y maven
```

Chocolatey では、 mysql 5.6 がインストールできなかったので、手動でインストール。


