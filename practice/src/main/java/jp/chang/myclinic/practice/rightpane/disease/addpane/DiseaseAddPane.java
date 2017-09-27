package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.practice.lib.dateinput.DateInputForm;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;

public class DiseaseAddPane extends JPanel {

    public DiseaseAddPane(int width, int patientId){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        DateInputForm startDateInput = new DateInputForm(Gengou.Current);
        startDateInput.setValue(LocalDate.now());
        SearchArea searchArea = new SearchArea(width, startDateInput);
        add(startDateInput, "wrap");
        add(searchArea);
    }

}
