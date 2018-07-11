package jp.chang.myclinic.dto;

public class ShinryouAttrDTO {

    public int shinryouId;
    public String tekiyou;
    public String shoujouShouki;

    @Override
    public String toString() {
        return "ShinryouAttrDTO{" +
                "shinryouId=" + shinryouId +
                ", tekiyou='" + tekiyou + '\'' +
                ", shoujouShouki='" + shoujouShouki + '\'' +
                '}';
    }
}
