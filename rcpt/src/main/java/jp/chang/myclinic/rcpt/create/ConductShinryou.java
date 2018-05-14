package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonProperty;

class ConductShinryou {

    @JsonProperty("診療コード")
    int shinryoucode;
    @JsonProperty("名称")
    String name;
    @JsonProperty("点数")
    int tensuu;

    ConductShinryou() {

    }

}
