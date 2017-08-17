package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2DTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class DispRecords extends JPanel {

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
        setLayout(makeLayout());
        visits.forEach(visitFull -> {
            Record record = new Record(visitFull, currentVisitId, tempVisitId);
            add(record, "growx, wrap");
        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
