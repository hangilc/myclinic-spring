package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.drug.DrugFormGetter;
import jp.chang.myclinic.practice.lib.drug.DrugFormHelper;
import jp.chang.myclinic.practice.lib.drug.DrugFormSetter;
import jp.chang.myclinic.practice.lib.drug.DrugInputConstraints;
import jp.chang.myclinic.util.PrescExampleUtil;

public class ExampleSearchResult implements DrugSearchResultModel {

    private PrescExampleFullDTO example;
    private String at;

    public ExampleSearchResult(PrescExampleFullDTO example, String at){
        this.example = example;
        this.at = at;
    }

    @Override
    public String rep() {
        return PrescExampleUtil.rep(example);
    }

    public PrescExampleFullDTO getPrescExample(){
        return example;
    }

    public String getVisitedAt(){
        return at;
    }

}
