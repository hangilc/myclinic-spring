package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFullPageDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class LeftPane extends JPanel {

    private PatientDTO patient;
    private VisitFullPageDTO visitFullPage;
    private RecordsNav topNav;
    private RecordsNav bottomNav;
    private DispRecords dispRecords;
    private JScrollPane scrollPane;

    public LeftPane(PatientDTO patient, VisitFullPageDTO visitFullPage){
        this.patient = patient;
        this.visitFullPage = visitFullPage;
        topNav = makeNav();
        bottomNav = makeNav();
        dispRecords = new DispRecords();
        dispRecords.setVisits(visitFullPage.visits);
        scrollPane = new JScrollPane(dispRecords);
        setLayout(new MigLayout("insets 0, fill", "[grow]", "[] [] [] [grow] []"));
        add(new PatientInfoPane(patient), "growx, wrap");
        add(new PatientManip(), "wrap");
        add(topNav, "wrap");
        add(scrollPane, "grow, wrap");
        add(bottomNav, "");
    }

    private RecordsNav makeNav(){
        return new RecordsNav(visitFullPage.page, visitFullPage.totalPages, this::onNavTrigger);
    }

    private void onNavTrigger(int newPage){
        Service.api.listVisitFull(patient.patientId, newPage)
                .thenAccept(visitPage -> {
                    dispRecords.setVisits(visitPage.visits);
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
