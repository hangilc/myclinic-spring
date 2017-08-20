package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.MainExecContext;
import jp.chang.myclinic.practice.leftpane.title.Title;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class Record extends JPanel {

    Title title;

    Record(VisitFull2DTO fullVisit, MainExecContext mainExecContext){
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        title = new Title(fullVisit.visit, mainExecContext);
        add(title, "growx, wrap");
    }
}
