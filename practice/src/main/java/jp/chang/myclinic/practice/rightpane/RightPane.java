package jp.chang.myclinic.practice.rightpane;

import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.practice.MainContext;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.WorkArea;
import jp.chang.myclinic.practice.rightpane.disease.DiseaseBox;
import jp.chang.myclinic.practice.rightpane.searchpatient.SearchPatient;
import jp.chang.myclinic.practice.rightpane.selectvisit.SelectVisit;
import jp.chang.myclinic.practice.rightpane.todaysvisits.TodaysVisits;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class RightPane extends JPanel {

    private int width;
    private WorkArea diseaseWorkArea;
    private DiseaseBox diseaseBox;

    public RightPane(){
        setLayout(new MigLayout("insets 0, hidemode 3", "[grow]", ""));
        EventQueue.invokeLater(this::setupComponents);
    }

    private void setupComponents(){
        this.width = getWidth();
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
        SearchPatient searchPatient = new SearchPatient();
        TodaysVisits todaysVisits = new TodaysVisits();
        add(diseaseWorkArea, "wrap");
        add(selectVisit, "growx, wrap");
        add(searchPatient, "growx, wrap");
//        {
//            JPanel frame = new JPanel(new MigLayout("insets 0, fill", "", ""));
//            frame.setBorder(BorderFactory.createTitledBorder("患者検索"));
//            SearchPatient searchPatientPane = new SearchPatient();
//            frame.add(searchPatientPane, "growx");
//            add(frame, "growx, wrap");
//        }
        add(todaysVisits, "sizegroup btn, wrap");
        add(todaysVisits.getWorkArea(), "w 10, h 260, growx, wrap");
    }

//    public RightPane(int width){
//        this.width = width;
//        setLayout(new MigLayout("insets 0, hidemode 3", "[grow]", ""));
//        setupDisease();
//        SelectVisit selectVisit = new SelectVisit();
//        RightPane self = this;
//        selectVisit.setCallback(new SelectVisit.Callback(){
//            @Override
//            public void onSelect(WqueueFullDTO wqueue) {
//                MainContext mainContext = MainContext.get(self);
//                mainContext.startExam(wqueue.patient, wqueue.visit, () -> {});
//            }
//        });
//        TodaysVisits todaysVisits = new TodaysVisits();
//        add(diseaseWorkArea, "wrap");
//        add(selectVisit, "growx, wrap");
//        {
//            JPanel frame = new JPanel(new MigLayout("insets 0, fill", "", ""));
//            frame.setBorder(BorderFactory.createTitledBorder("患者検索"));
//            SearchPatient searchPatientPane = new SearchPatient();
//            frame.add(searchPatientPane, "growx");
//            add(frame, "top, growx, wrap");
//        }
//        add(todaysVisits, "sizegroup btn, wrap");
//        add(todaysVisits.getWorkArea(), "w 10, growx, wrap");
//    }

    public void openDisease(int patientId){
        Service.api.listCurrentDiseaseFull(patientId)
                .thenAccept(list -> EventQueue.invokeLater(() -> {
                    diseaseBox.reset(patientId, list);
                    diseaseWorkArea.setVisible(true);
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
        diseaseWorkArea.setVisible(true);
    }

    public void closeDisease(){
        diseaseBox.clear();
        diseaseWorkArea.setVisible(false);
    }

    private void setupDisease(){
        diseaseWorkArea = new WorkArea(width, "症病名");
        diseaseBox = new DiseaseBox(diseaseWorkArea.getInnerColumnWidth());
        diseaseWorkArea.setComponent(diseaseBox);
        diseaseWorkArea.setVisible(false);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
