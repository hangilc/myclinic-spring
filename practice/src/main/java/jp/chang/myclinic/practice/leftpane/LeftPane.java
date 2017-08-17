package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2PageDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class LeftPane extends JPanel {

    public interface Callback {
        void onFinishPatient();
    }

    private PatientDTO patient;
    private VisitFull2PageDTO visitFullPage;
    private RecordsNav topNav;
    private RecordsNav bottomNav;
    private DispRecords dispRecords;
    private JScrollPane scrollPane;

    public LeftPane(PatientDTO patient, VisitFull2PageDTO visitFullPage, int currentVisitId, int tempVisitId,
                    Callback callback){
        this.patient = patient;
        this.visitFullPage = visitFullPage;
        topNav = makeNav(currentVisitId, tempVisitId);
        bottomNav = makeNav(currentVisitId, tempVisitId);
        dispRecords = new DispRecords();
        dispRecords.setVisits(visitFullPage.visits, currentVisitId, tempVisitId);
        scrollPane = new JScrollPane(dispRecords);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        PatientManip patientManip = new PatientManip(new PatientManip.Callback(){
            @Override
            public void onFinishPatient() {
                callback.onFinishPatient();
            }
        });
        setLayout(new MigLayout("insets 0, fill", "[grow]", "[] [] [] [grow] []"));
        add(new PatientInfoPane(patient), "growx, wrap");
        add(patientManip, "wrap");
        add(topNav, "wrap");
        add(scrollPane, "grow, wrap");
        add(bottomNav, "");
    }

    private RecordsNav makeNav(int currentVisitId, int tempVisitId){
        return new RecordsNav(visitFullPage.page, visitFullPage.totalPages,
                newPage -> onNavTrigger(newPage, currentVisitId, tempVisitId));
    }

    private void onNavTrigger(int newPage, int currentVisitId, int tempVisitId){
        Service.api.listVisitFull2(patient.patientId, newPage)
                .thenAccept(visitPage -> {
                    dispRecords.setVisits(visitPage.visits, currentVisitId, tempVisitId);
                    topNav.set(newPage, visitPage.totalPages);
                    bottomNav.set(newPage, visitPage.totalPages);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
