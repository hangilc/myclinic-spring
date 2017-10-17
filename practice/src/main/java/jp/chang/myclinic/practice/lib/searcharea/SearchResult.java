package jp.chang.myclinic.practice.lib.searcharea;

import jp.chang.myclinic.practice.lib.SearchResultList;

class SearchResult extends SearchResultList<SearchResultData> {

    @Override
    protected String dataToRep(SearchResultData data) {
        return data.getRep();
    }


}

