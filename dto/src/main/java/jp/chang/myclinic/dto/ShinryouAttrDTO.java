package jp.chang.myclinic.dto;

public class ShinryouAttrDTO {

    public int shinryouId;
    public String tekiyou;
    public String shoujouShouki;

    public static ShinryouAttrDTO copy(ShinryouAttrDTO src){
        ShinryouAttrDTO dst = new ShinryouAttrDTO();
        dst.shinryouId = src.shinryouId;
        dst.tekiyou = src.tekiyou;
        dst.shoujouShouki = src.shoujouShouki;
        return dst;
    }

    @Override
    public String toString() {
        return "ShinryouAttrDTO{" +
                "shinryouId=" + shinryouId +
                ", tekiyou='" + tekiyou + '\'' +
                ", shoujouShouki='" + shoujouShouki + '\'' +
                '}';
    }
}
