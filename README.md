# myclinic-spring

個人開業医用のソフトウェアです。電子カルテ管理、受付業務、院内薬局業務、レセプト印刷などの機能があり、これだけで、医院の運営が可能であり、実際に実地運用を行っています。

## 概要

実装は Java 言語で行っています。構成は、一つのバックエンド・サーバーと各種のデスクトップ・プログラムから成り立っています。以下、それぞれの説明です。

### サーバー

Spring Boot を使用した、バックエンド・サーバーです。MySQL データベースにアクセスし、すべての業務行為を実装しています。機能は、HTTP REST サービスとして公開されており、
他のデスクトップ・プログラムはこのインターフェースを通して、業務の実行を行います。

### 電子カルテ

JavFX で実装された、デスクトップ・プログラムです。診療に関連した、文章入力、院内処方、処方せん発行、診療報酬計算、自己負担額計算、紹介状作成などが行えます。
ユーザーからの入力をもとにして、サーバーに HTTP Client を通じてリクエストを発行し、これらの業務を行います。

（作成中)

## ライセンス(License)

[MIT License]("./LICENSE")
