package jp.chang.myclinic.pharma;

import javax.swing.*;
import java.util.List;

public class AuxDispVisits extends JPanel {
    private int patientId;

    public AuxDispVisits(int patientId){
        this.patientId = patientId;
        Service.api.listVisitIdVisitedAtForPatient(patientId)
                .thenAccept(visitIds -> {
                    List<RecordPage> pages = RecordPage.divideToPages(visitIds);
                    System.out.println(pages);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    return null;
                });
    }
}
