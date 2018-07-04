Title: 新規インストール
Date: 2018-06-25 15:58
Category: development
Status: draft

myclinic-spring を新しいＰＣにインストールしてみる。今回使ったＰＣは Lenovo ノートパソコン ideapad 320 (Core i5, 8GB Memory, 256GB SSD) で、価格は 76,400 円だった。

不要なソフトをアンインストールして、Windows の更新プログラムをインストール（２時間かかった）。

Chocholatey をインストール。cmd.exe を管理者として起動。

Temporary directory を作成し、そこに移動。
```shell
> cd C:\
> mkdir temp
> cd temp
```

Chocolatey をインストール。
```shell
>  @"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
```

JDK10 のインストール

```shell
> choco install -y jdk10
```

GIT のインストール

```shell
> choco install -y git
> setx /M PATH "%PATH%;C:\Program Files\Git\bin"
```

MySQL のインストール

```shell
> choco install -y mysql
> choco install -y vcredist2013 --version 12.0.0.20140222
```

Curl のインストール

```shell
> choco install -y curl
```

Maven のインストール

```shell
> curl http://ftp.kddilabs.jp/infosystems/apache/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.zip >maven.zip
> 7z x maven.zip -omaven
> move maven\apache-maven-3.5.4 C:\maven
> setx /M PATH "%PATH%;C:\maven\bin"
```

Python のインストール

```shell
> choco install -y python
```

クリーンアップ

```shell
> cd C:\
> rmdir /S /Q C:\temp
```

以上で、依存プログラムのインストールが完了したので、管理者として実行している cmd.exe を閉じる。

通常のユーザーとして cmd.exe を起動する。

```shell
> cd %HOMEDRIVE%%HOMEPATH%
```

myclinic-spring.git をクローン

```shell
> git clone https://github.com/hangilc/myclinic-spring.git
```


