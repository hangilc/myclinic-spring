package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonProperty;

class ConductDrug {

    @JsonProperty("医薬品コード")
    int iyakuhincode;
    @JsonProperty("名称")
    String name;
    @JsonProperty("用量")
    double amount;
    @JsonProperty("単位")
    String unit;
    @JsonProperty("薬価")
    double yakka;

    ConductDrug() {

    }

}
