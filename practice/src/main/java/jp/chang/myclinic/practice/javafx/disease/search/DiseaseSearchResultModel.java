package jp.chang.myclinic.practice.javafx.disease.search;

import jp.chang.myclinic.practice.javafx.disease.add.DiseaseInput;

public interface DiseaseSearchResultModel {
    String rep();
    void applyTo(DiseaseInput diseaseInput);
}
