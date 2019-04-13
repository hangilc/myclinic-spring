package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

public class ShinryouAttrDTO {
    @Primary
    public int shinryouId;
    public String tekiyou;

    public static ShinryouAttrDTO copy(ShinryouAttrDTO src){
        ShinryouAttrDTO dst = new ShinryouAttrDTO();
        dst.shinryouId = src.shinryouId;
        dst.tekiyou = src.tekiyou;
        return dst;
    }

    public static boolean isEmpty(ShinryouAttrDTO attr){
        return attr.tekiyou == null || attr.tekiyou.isEmpty();
    }

    public static String extractTekiyou(ShinryouAttrDTO attr){
        return attr == null ? null : attr.tekiyou;
    }

    @Override
    public String toString() {
        return "ShinryouAttrDTO{" +
                "shinryouId=" + shinryouId +
                ", tekiyou='" + tekiyou + '\'' +
                '}';
    }
}
