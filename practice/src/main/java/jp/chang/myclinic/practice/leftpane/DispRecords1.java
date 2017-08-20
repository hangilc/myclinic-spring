package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DispRecords1 extends JPanel {

    private Map<Integer, Record1> recordMap = new HashMap<>();

    DispRecords1(){
        setLayout(makeLayout());
        add(new JLabel("平成２９年７月２９日"), "span, wrap");
        add(new JLabel("left"), "");
        add(new JLabel("right right right right"), "wrap");
    }

    private MigLayout makeLayout(){
        return new MigLayout("insets 0, fillx", "[grow]", "");
    }

    void setVisits(List<VisitFull2DTO> visits, int currentVisitId, int tempVisitId){
        removeAll();
        recordMap.clear();
        setLayout(makeLayout());
        visits.forEach(visitFull -> {
            Record1 record1 = new Record1(visitFull, currentVisitId, tempVisitId);
            record1.setCallback(new Record1.Callback(){
                @Override
                public void onCopyAllDrugs(int targetVisitId, List<Integer> drugIds) {
                    handleCopyAllDrugs(targetVisitId, drugIds);
                }
            });
            add(record1, "growx, wrap");
            recordMap.put(visitFull.visit.visitId, record1);
        });
    }

    private void handleCopyAllDrugs(int targetVisitId, List<Integer> drugIds){
        Record1 targetRecord1 = recordMap.getOrDefault(targetVisitId, null);
        if( targetRecord1 != null ){
            Service.api.listDrugFullByDrugIds(drugIds)
                    .thenAccept(drugs -> {
                        EventQueue.invokeLater(() -> {
                            targetRecord1.appendDrugs(drugs);
                        });
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });

        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
