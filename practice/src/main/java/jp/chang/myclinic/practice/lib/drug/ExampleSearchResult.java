package jp.chang.myclinic.practice.lib.drug;

import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
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

    @Override
    public void stuffInto(DrugFormSetter target, DrugFormGetter getter, DrugInputConstraints constraints) {
        int origIyakuhincode = example.master.iyakuhincode;
        PracticeLib.resolveIyakuhinMaster(origIyakuhincode, at, master -> {
            PrescExampleFullDTO resolved = PrescExampleFullDTO.copy(example);
            resolved.master = master;
            DrugFormHelper.setExample(target, resolved, getter, constraints);
        });
    }

}
