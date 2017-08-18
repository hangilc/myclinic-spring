package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DispRecords extends JPanel {

    private Map<Integer, Record> recordMap = new HashMap<>();

    DispRecords(){
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
            Record record = new Record(visitFull, currentVisitId, tempVisitId);
            record.setCallback(new Record.Callback(){
                @Override
                public void onCopyAllDrugs(int targetVisitId, List<Integer> drugIds) {
                    handleCopyAllDrugs(targetVisitId, drugIds);
                }
            });
            add(record, "growx, wrap");
            recordMap.put(visitFull.visit.visitId, record);
        });
    }

    private void handleCopyAllDrugs(int targetVisitId, List<Integer> drugIds){
        Record targetRecord = recordMap.getOrDefault(targetVisitId, null);
        if( targetRecord != null ){
            Service.api.listDrugFullByDrugIds(drugIds)
                    .thenAccept(drugs -> {
                        EventQueue.invokeLater(() -> {
                            targetRecord.appendDrugs(drugs);
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
