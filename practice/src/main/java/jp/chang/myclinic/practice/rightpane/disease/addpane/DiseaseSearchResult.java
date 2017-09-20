package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.practice.lib.SearchResultList;

class DiseaseSearchResult extends SearchResultList<SearchResultData> {

    @Override
    protected String dataToRep(SearchResultData data) {
        return data.getRep();
    }

}

