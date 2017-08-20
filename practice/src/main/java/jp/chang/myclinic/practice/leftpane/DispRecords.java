package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.MainExecContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class DispRecords extends JPanel {

    private MainExecContext mainExecContext;

    DispRecords(int width, MainExecContext mainExecContext){
        this.mainExecContext = mainExecContext;
        setLayout(new MigLayout("insets 0", "[grow]", ""));
    }

    void setVisits(List<VisitFull2DTO> visits){
        int width = getSize().width;
        System.out.println("DispRecords width: " + width);
        removeAll();
        visits.forEach(visit -> {
            Record record = new Record(visit, width, mainExecContext);
            add(record, "growx, wrap");
        });
        repaint();
        revalidate();
    }
}
