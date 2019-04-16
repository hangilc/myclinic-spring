package jp.chang.myclinic.dto;

public class DrugWithAttrDTO {

    public DrugDTO drug;
    public DrugAttrDTO attr;

    @Override
    public String toString() {
        return "DrugWithAttrDTO{" +
                "drug=" + drug +
                ", attr=" + attr +
                '}';
    }
}
