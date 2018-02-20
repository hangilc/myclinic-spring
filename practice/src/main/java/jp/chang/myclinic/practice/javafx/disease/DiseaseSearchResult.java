package jp.chang.myclinic.practice.javafx.disease;

import jp.chang.myclinic.practice.javafx.parts.searchbox.SearchResultBase;

public abstract class DiseaseSearchResult extends SearchResultBase<DiseaseSearchResultModel>  {

    @Override
    protected String convert(DiseaseSearchResultModel model) {
        return model.rep();
    }

}
