package jp.chang.myclinic.practice.rightpane.disease.endpane;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DateManipPart extends JPanel {
    DateManipPart(){
        setLayout(new MigLayout("insets 0", "", ""));
        Link weekLink = new Link("週");
        Link todayLink = new Link("今日");
        Link endOfMonthLink = new Link("月末");
        Link endOfLastMonthLink = new Link("先月末");
        add(weekLink);
        add(new JLabel("|"));
        add(todayLink);
        add(new JLabel("|"));
        add(endOfMonthLink);
        add(new JLabel("|"));
        add(endOfLastMonthLink);
    }
}
