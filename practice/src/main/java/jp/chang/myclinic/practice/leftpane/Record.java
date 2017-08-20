package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.MainExecContext;
import jp.chang.myclinic.practice.leftpane.text.TextArea;
import jp.chang.myclinic.practice.leftpane.title.Title;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class Record extends JPanel {

    private Title title;
    private TextArea textArea;

    Record(VisitFull2DTO fullVisit, int width, MainExecContext mainExecContext){
        int colWidth = (width - 4) / 2;
        int gap = width - colWidth * 2;
        setLayout(new MigLayout("insets 0, debug",
                String.format("[%dpx!]%d[%dpx!]", colWidth, gap, colWidth),
                ""));
        title = new Title(fullVisit.visit, mainExecContext);
        textArea = new TextArea(fullVisit, colWidth);
        add(title, "span, growx, wrap");
        add(textArea);
        add(new JLabel("right"));
    }
}
