package jp.chang.myclinic.dto;

public class DrugAttrDTO {

    public int drugId;
    public String tekiyou;

    public static DrugAttrDTO copy(DrugAttrDTO src){
        DrugAttrDTO dst = new DrugAttrDTO();
        dst.drugId = src.drugId;
        dst.tekiyou = src.tekiyou;
        return dst;
    }

    @Override
    public String toString() {
        return "DrugAttrDTO{" +
                "drugId=" + drugId +
                ", tekiyou='" + tekiyou + '\'' +
                '}';
    }
}
