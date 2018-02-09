package jp.chang.myclinic.practice.lib.drug;

public interface DrugSearchResultModel {
    String rep();
    void stuffInto(DrugFormSetter target, DrugFormGetter getter, DrugInputConstraints constraints);
}
