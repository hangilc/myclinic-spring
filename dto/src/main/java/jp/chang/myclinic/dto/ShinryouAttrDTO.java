package jp.chang.myclinic.dto;

public class ShinryouAttrDTO {

    public int shinryouId;
    public String tekiyou;

    public static ShinryouAttrDTO copy(ShinryouAttrDTO src){
        ShinryouAttrDTO dst = new ShinryouAttrDTO();
        dst.shinryouId = src.shinryouId;
        dst.tekiyou = src.tekiyou;
        return dst;
    }

    @Override
    public String toString() {
        return "ShinryouAttrDTO{" +
                "shinryouId=" + shinryouId +
                ", tekiyou='" + tekiyou + '\'' +
                '}';
    }
}
