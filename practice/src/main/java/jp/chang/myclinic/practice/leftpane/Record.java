package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.MainExecContext;
import jp.chang.myclinic.practice.leftpane.title.Title;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class Record extends JPanel {

    Title title;

    Record(VisitFull2DTO fullVisit, int width, MainExecContext mainExecContext){
        int colWidth = width - 4;
        int gap = width - colWidth * 2;
        System.out.println("colWidth: " + colWidth);
        setLayout(new MigLayout("insets 0, debug",
                String.format("[%dpx!]%d[%dpx!]", colWidth, gap, colWidth),
                ""));
        title = new Title(fullVisit.visit, mainExecContext);
        add(title, "span, growx, wrap");
        add(new JLabel("left"));
        add(new JLabel("right"));
    }
}
