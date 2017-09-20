package jp.chang.myclinic.practice.rightpane;

import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.practice.MainContext;
import jp.chang.myclinic.practice.leftpane.WorkArea;
import jp.chang.myclinic.practice.rightpane.disease.DiseaseBox;
import jp.chang.myclinic.practice.rightpane.selectvisit.SelectVisit;
import jp.chang.myclinic.practice.rightpane.todaysvisits.TodaysVisits;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;

public class RightPane extends JPanel {

    private int width;
    private WorkArea diseaseWorkArea;
    private DiseaseBox diseaseBox;

    public RightPane(int width){
        this.width = width;
        setLayout(new MigLayout("insets 0, hidemode 3", "[grow]", ""));
        setupDisease();
        SelectVisit selectVisit = new SelectVisit();
        RightPane self = this;
        selectVisit.setCallback(new SelectVisit.Callback(){
            @Override
            public void onSelect(WqueueFullDTO wqueue) {
                MainContext mainContext = MainContext.get(self);
                mainContext.startExam(wqueue.patient, wqueue.visit, () -> {});
            }
        });
        TodaysVisits todaysVisits = new TodaysVisits();
        add(diseaseWorkArea, "wrap");
        add(selectVisit, "growx, wrap");
        {
            JPanel frame = new JPanel(new MigLayout("insets 0, fill", "", ""));
            frame.setBorder(BorderFactory.createTitledBorder("患者検索"));
            SearchPatient searchPatientPane = new SearchPatient();
            frame.add(searchPatientPane, "growx");
            add(frame, "top, growx, wrap");
        }
        add(todaysVisits, "sizegroup btn, wrap");
        add(todaysVisits.getWorkArea(), "w 10, growx, wrap");
    }

    public void openDisease(int patientId, LocalDate at){
        diseaseWorkArea.setVisible(true);
    }

    public void closeDisease(){
        diseaseWorkArea.setVisible(false);
    }

    private void setupDisease(){
        diseaseWorkArea = new WorkArea(width, "症病名");
        diseaseBox = new DiseaseBox(diseaseWorkArea.getInnerColumnWidth());
        diseaseWorkArea.setVisible(false);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
