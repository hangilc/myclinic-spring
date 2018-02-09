package jp.chang.myclinic.practice.lib;

public interface DrugSearchResultModel {
    String rep();
    void stuffInto(DrugFormSetter form, DrugInputConstraints constraints);
}
