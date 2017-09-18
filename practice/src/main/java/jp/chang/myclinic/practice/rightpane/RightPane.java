package jp.chang.myclinic.practice.rightpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.practice.MainExecContext;
import jp.chang.myclinic.practice.rightpane.selectvisit.SelectVisit;
import jp.chang.myclinic.practice.rightpane.todaysvisits.TodaysVisits;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class RightPane extends JPanel {

    public RightPane(MainExecContext mainExecContext){
        setLayout(new MigLayout("hidemode 3", "[grow]", ""));
        SelectVisit selectVisit = new SelectVisit();
        selectVisit.setCallback(new SelectVisit.Callback(){
            @Override
            public void onSelect(WqueueFullDTO wqueue) {
                mainExecContext.startExam(wqueue.visit, wqueue.patient)
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });

            }
        });
        TodaysVisits todaysVisits = new TodaysVisits();
        add(selectVisit, "growx, wrap");
        {
            JPanel frame = new JPanel(new MigLayout("insets 0, fill", "", ""));
            frame.setBorder(BorderFactory.createTitledBorder("患者検索"));
            SearchPatient searchPatientPane = new SearchPatient(new SearchPatient.Context(){
                @Override
                public CompletableFuture<Boolean> startPatient(PatientDTO patient) {
                    return mainExecContext.startExam(null, patient);
                }
            });
            frame.add(searchPatientPane, "growx");
            add(frame, "top, growx, wrap");
        }
        add(todaysVisits, "sizegroup btn, wrap");
        add(todaysVisits.getWorkArea(), "wrap");
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
