package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

public class DrugAttrDTO {
    @Primary
    public int drugId;
    public String tekiyou;

    public static DrugAttrDTO copy(DrugAttrDTO src){
        DrugAttrDTO dst = new DrugAttrDTO();
        dst.drugId = src.drugId;
        dst.tekiyou = src.tekiyou;
        return dst;
    }

    public static boolean isEmpty(DrugAttrDTO attr){
        return attr.tekiyou == null || attr.tekiyou.isEmpty();
    }

    @Override
    public String toString() {
        return "DrugAttrDTO{" +
                "drugId=" + drugId +
                ", tekiyou='" + tekiyou + '\'' +
                '}';
    }
}
