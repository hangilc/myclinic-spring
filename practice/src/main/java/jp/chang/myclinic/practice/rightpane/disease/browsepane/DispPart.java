package jp.chang.myclinic.practice.rightpane.disease.browsepane;

import jp.chang.myclinic.dto.DiseaseFullDTO;

import javax.swing.*;

class DispPart extends JPanel {

    DispPart(int width){

    }

    void setData(DiseaseFullDTO disease){
        System.out.println(disease);
    }
}
