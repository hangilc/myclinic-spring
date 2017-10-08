package jp.chang.myclinic.practice.rightpane.disease.browsepane;

import jp.chang.myclinic.dto.DiseaseFullDTO;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

class ListPart extends JScrollPane {

    private ResultList resultList;

    ListPart(int itemsPerPage, List<DiseaseFullDTO> diseases){
        resultList = new ResultList(itemsPerPage);
        setViewportView(resultList);
        setData(diseases);
    }

    void setData(List<DiseaseFullDTO> diseases){
        resultList.setListData(diseases.toArray(new DiseaseFullDTO[]{}));
    }

    void setSelectionHandler(Consumer<DiseaseFullDTO> handler){
        resultList.setSelectionHandler(handler);
    }
}
