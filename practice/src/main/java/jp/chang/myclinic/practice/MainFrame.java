package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.rightpane.SearchPatient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class MainFrame extends JFrame {

    MainFrame(){
        setTitle("診察");
        setLayout(new MigLayout("", "", ""));
        add(makeLeftPane(), "w 460, h 300, grow");
        add(makeRightPane(), "w 220, h 300, grow");
        pack();
    }

    private JComponent makeLeftPane(){
        JPanel pane = new JPanel(new MigLayout("insets 0, fill", "", ""));
        return pane;
    }

    private JComponent makeRightPane(){
        JPanel pane = new JPanel(new MigLayout("insets 0 0 0 24, fill", "", ""));
        {
            JPanel frame = new JPanel(new MigLayout("insets 0, fill", "", ""));
            frame.setBorder(BorderFactory.createTitledBorder("患者検索"));
            frame.add(new SearchPatient(patient -> doStartPatient(patient)), "growx");
            pane.add(frame, "top, growx, wrap");
        }
        JScrollPane sp = new JScrollPane(pane);
        sp.setBorder(null);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        return sp;
    }

    private void doStartPatient(PatientDTO patient){
        System.out.println("starting patient: " + patient);
    }
}
