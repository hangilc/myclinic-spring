package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConductShinryou {

    @JsonProperty("診療コード")
    public int shinryoucode;
    @JsonProperty("名称")
    public String name;
    @JsonProperty("点数")
    public int tensuu;


}
