package jp.chang.myclinic.practice;

import jp.chang.myclinic.practice.rightpane.SearchPatient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class MainFrame extends JFrame {

    MainFrame(){
        setTitle("診察");
        setLayout(new MigLayout("", "", ""));
        add(makeLeftPane(), "w 460");
        add(makeRightPane(), "w 200");
        pack();
    }

    private JComponent makeLeftPane(){
        JPanel pane = new JPanel(new MigLayout("insets 0, fill", "", ""));
        return pane;
    }

    private JComponent makeRightPane(){
        JPanel pane = new JPanel(new MigLayout("insets 0, fill", "", ""));
        pane.add(new SearchPatient(), "growx, wrap");
        return pane;
    }
}
