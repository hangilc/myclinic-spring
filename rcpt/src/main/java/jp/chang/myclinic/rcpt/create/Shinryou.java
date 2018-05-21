package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Shinryou {

    private static Logger logger = LoggerFactory.getLogger(Shinryou.class);

    @JsonProperty("診療コード")
    int shinryoucode;
    @JsonProperty("名称")
    String name;
    @JsonProperty("点数")
    int tensuu;
    @JsonProperty("集計先")
    String shuukeisaki;
    @JsonProperty("包括検査")
    String houkatsuKensa;
    @JsonProperty("検査グループ")
    String kensaGroup;

    Shinryou() {

    }

    public int getShinryoucode() {
        return shinryoucode;
    }

    public String getName() {
        return name;
    }

    public int getTensuu() {
        return tensuu;
    }

    public String getShuukeisaki() {
        return shuukeisaki;
    }

    public String getHoukatsuKensa() {
        return houkatsuKensa;
    }

    public String getKensaGroup() {
        return kensaGroup;
    }

    @Override
    public String toString() {
        return "Shinryou{" +
                "shinryoucode=" + shinryoucode +
                ", name='" + name + '\'' +
                ", tensuu=" + tensuu +
                ", shuukeisaki='" + shuukeisaki + '\'' +
                ", houkatsuKensa='" + houkatsuKensa + '\'' +
                ", kensaGroup='" + kensaGroup + '\'' +
                '}';
    }
}
