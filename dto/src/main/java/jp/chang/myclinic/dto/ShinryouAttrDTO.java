package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

public class ShinryouAttrDTO {
    @Primary
    public int shinryouId;
    public String tekiyou;

    public static ShinryouAttrDTO copy(ShinryouAttrDTO src){
        if( src == null ){
            return null;
        }
        ShinryouAttrDTO dst = new ShinryouAttrDTO();
        dst.shinryouId = src.shinryouId;
        dst.tekiyou = src.tekiyou;
        return dst;
    }

    public static ShinryouAttrDTO create(int shinryouId){
        return create(shinryouId, null);
    }

    public static ShinryouAttrDTO create(int shinryouId, String tekiyou){
        ShinryouAttrDTO attr = new ShinryouAttrDTO();
        attr.shinryouId = shinryouId;
        attr.tekiyou = tekiyou;
        return attr;
    }

    public static boolean isEmpty(ShinryouAttrDTO attr){
        return attr.tekiyou == null || attr.tekiyou.isEmpty();
    }

    public static String extractTekiyou(ShinryouAttrDTO attr){
        return attr == null ? null : attr.tekiyou;
    }

    public static ShinryouAttrDTO setTekiyou(int shinryouId, ShinryouAttrDTO attr, String tekiyou){
        if( attr == null ){
            return create(shinryouId, tekiyou);
        } else {
            attr.tekiyou = tekiyou;
            if( isEmpty(attr) ){
                attr = null;
            }
            return attr;
        }
    }

    @Override
    public String toString() {
        return "ShinryouAttrDTO{" +
                "shinryouId=" + shinryouId +
                ", tekiyou='" + tekiyou + '\'' +
                '}';
    }
}
