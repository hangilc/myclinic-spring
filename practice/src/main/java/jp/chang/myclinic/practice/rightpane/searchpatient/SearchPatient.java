package jp.chang.myclinic.practice.rightpane.searchpatient;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.MainContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class SearchPatient extends JPanel {

    private Workplace workplace = new Workplace();

    public SearchPatient(){
        Component self = this;
        setLayout(new MigLayout("insets 0, hidemode 3", "[grow]", ""));
        JButton searchButton = new JButton("患者検索");
        searchButton.addActionListener(evt -> {
            if( workplace.isVisible() ){
                workplace.setVisible(false);
            } else {
                workplace.setVisible(true);
            }
        });
        workplace.setVisible(false);
        workplace.setCallback(new Workplace.Callback() {
            @Override
            public void onSelect(PatientDTO patient) {
                MainContext.get(self).startBrowse(patient, () -> {
                    workplace.setVisible(false);
                    workplace.clear();
                });
            }
        });
        add(searchButton);
        add(workplace, "newline, growx");
    }

}
