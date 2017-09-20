package jp.chang.myclinic.practice.rightpane.disease.addpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class DiseaseAddPane extends JPanel {

    public DiseaseAddPane(int width){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        SearchArea searchArea = new SearchArea(width);
        add(searchArea);
    }
}
