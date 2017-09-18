package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.MainExecContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class DispRecords extends JPanel {

    private MainExecContext mainExecContext;
    private Map<Integer, Record> recordMap = new LinkedHashMap<>();

    DispRecords(int width, MainExecContext mainExecContext){
        this.mainExecContext = mainExecContext;
        setLayout(new MigLayout("insets 0", "[grow]", ""));
    }

    void setVisits(List<VisitFull2DTO> visits){
        int width = getSize().width;
        removeAll();
        recordMap.clear();
        visits.forEach(visit -> {
            Record record = new Record(visit, width, mainExecContext);
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

    void appendShinryou(int visitId, List<ShinryouFullDTO> entered, Runnable uiCallback) {
        Record record = recordMap.getOrDefault(visitId, null);
        if( record != null ){
            record.appendShinryou(entered);
            uiCallback.run();
        }
    }

    void appendConduct(int visitId, List<ConductFullDTO> entered, Runnable uiCallback) {
        Record record = recordMap.getOrDefault(visitId, null);
        if( record != null ){
            record.appendConduct(entered);
            uiCallback.run();
        }
    }

}
