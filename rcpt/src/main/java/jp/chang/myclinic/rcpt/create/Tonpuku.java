package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tonpuku {

    @JsonProperty("医薬品コード")
    int iyakuhincode;
    @JsonProperty("名称")
    String name;
    @JsonProperty("用量")
    double amount;
    @JsonProperty("単位")
    String unit;
    @JsonProperty("用法")
    String usage;
    @JsonProperty("薬価")
    double yakka;
    @JsonProperty("麻毒")
    char madoku;
    @JsonProperty("回数")
    int days;

    Tonpuku() {

    }

    @Override
    public String toString() {
        return "Tonpuku{" +
                "iyakuhincode=" + iyakuhincode +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", usage='" + usage + '\'' +
                ", yakka=" + yakka +
                ", madoku=" + madoku +
                ", days=" + days +
                '}';
    }
}
