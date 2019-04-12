package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

public class DrugAttrDTO {
    @Primary
    public int drugId;
    public String tekiyou;

    public static DrugAttrDTO copy(DrugAttrDTO src) {
        DrugAttrDTO dst = new DrugAttrDTO();
        dst.drugId = src.drugId;
        dst.tekiyou = src.tekiyou;
        return dst;
    }

    public static boolean isEmpty(DrugAttrDTO attr) {
        return attr.tekiyou == null || attr.tekiyou.isEmpty();
    }

    public static String getTekiyou(DrugAttrDTO attr, String tekiyou){
        return attr == null ? null : attr.tekiyou;
    }

    public static DrugAttrDTO setTekiyou(int drugId, DrugAttrDTO attr, String tekiyou){
        if( attr == null ){
            attr = new DrugAttrDTO();
            attr.drugId = drugId;
        }
        attr.tekiyou = tekiyou;
        if( isEmpty(attr) ){
            attr = null;
        }
        return attr;
    }

    public static DrugAttrDTO deleteTekiyou(int drugId, DrugAttrDTO attr){
        return setTekiyou(drugId, attr, null);
    }

    @Override
    public String toString() {
        return "DrugAttrDTO{" +
                "drugId=" + drugId +
                ", tekiyou='" + tekiyou + '\'' +
                '}';
    }
}
