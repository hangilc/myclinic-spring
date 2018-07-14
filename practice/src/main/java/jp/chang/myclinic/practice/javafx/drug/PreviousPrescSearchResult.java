package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;

class PreviousPrescSearchResult implements DrugSearchResultModel {

    private DrugFullDTO drugFull;
    private String at;

    public PreviousPrescSearchResult(DrugFullDTO drug, String at){
        this.drugFull = drug;
        this.at = at;
    }

    public DrugFullDTO getDrug(){
        return drugFull;
    }

    public String getVisitedAt(){
        return at;
    }

    @Override
    public String rep() {
        return DrugUtil.drugRep(drugFull);
    }

}
