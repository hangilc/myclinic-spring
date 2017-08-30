package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.MainExecContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class DispRecords extends JPanel {

    private MainExecContext mainExecContext;
    private Map<Integer, Record> recordMap;

    DispRecords(int width, MainExecContext mainExecContext){
        this.mainExecContext = mainExecContext;
        setLayout(new MigLayout("insets 0", "[grow]", ""));
    }

    void setVisits(List<VisitFull2DTO> visits){
        int width = getSize().width;
        removeAll();
        if( recordMap == null ){
            this.recordMap = new LinkedHashMap<>();
        } else {
            recordMap.clear();
        }
        visits.forEach(visit -> {
            Record record = new Record(visit, width, mainExecContext);
            //bindRecord(record);
            add(record, "growx, wrap");
            recordMap.put(visit.visit.visitId, record);
        });
        repaint();
        revalidate();
    }

    void clear(){
        removeAll();
        recordMap.clear();
        repaint();
        revalidate();
    }

    void appendDrugs(int visitId, List<DrugFullDTO> drugs){
        Record record = recordMap.getOrDefault(visitId, null);
        if( record != null ){
            record.appendDrugs(drugs);
        }
    }

//    private void bindRecord(Record record){
//        record.setCallback(new Record.Callback(){
//            @Override
//            public void onDrugsCopied(int targetVisitId, List<DrugFullDTO> drugs) {
//                Record target = recordMap.getOrDefault(targetVisitId, null);
//                if( target != null ){
//                    target.addDrugs(drugs);
//                }
//            }
//        });
//    }
}
