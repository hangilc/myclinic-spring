package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonProperty;

class ConductKizai {

    @JsonProperty("器材コード")
    int kizaicode;
    @JsonProperty("名称")
    String name;
    @JsonProperty("量")
    double amount;
    @JsonProperty("単位")
    String unit;
    @JsonProperty("金額")
    double kingaku;

    ConductKizai() {

    }

}
