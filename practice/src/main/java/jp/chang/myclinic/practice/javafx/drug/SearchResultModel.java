package jp.chang.myclinic.practice.javafx.drug;

public interface SearchResultModel {
    String rep();
    void stuffInto(DrugInputModel model, InputConstraints constraints);
}
