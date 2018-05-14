package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
class Shinryou {

    private static Logger logger = LoggerFactory.getLogger(Shinryou.class);

    @JsonProperty("診療コード")
    int shinryoucode;
    @JsonProperty("名称")
    String name;
    @JsonProperty("点数")
    int tensuu;
    @JsonProperty("集計先")
    String shuukeisaki;
    @JsonProperty("検査グループ")
    String kensaGroup;

    Shinryou() {

    }

    @Override
    public String toString() {
        return "Shinryou{" +
                "shinryoucode=" + shinryoucode +
                ", name='" + name + '\'' +
                ", tensuu=" + tensuu +
                ", shuukeisaki='" + shuukeisaki + '\'' +
                ", kensaGroup='" + kensaGroup + '\'' +
                '}';
    }
}
