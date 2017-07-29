package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.leftpane.PatientInfoPane;
import jp.chang.myclinic.practice.rightpane.SearchPatient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class MainFrame extends JFrame {

    JPanel leftPanel;
    JScrollPane leftScroll;
    JPanel rightPanel;
    JScrollPane rightScroll;
    SearchPatient searchPatientPane;

    MainFrame(){
        setTitle("診察");
        setLayout(new MigLayout("fill", "", ""));
        add(makeLeftPane(), "w 460, h 260, grow");
        add(makeRightPane(), "w 220, h 300, grow");
        pack();
    }

    private JComponent makeLeftPane(){
        leftPanel = new JPanel(makeLeftPanelLayout());
        leftScroll = new JScrollPane(leftPanel);
        leftScroll.setBorder(null);
        leftScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return leftScroll;
    }

    private MigLayout makeLeftPanelLayout(){
        return new MigLayout("insets 0 0 0 24, fill", "", "");
    }

    private JComponent makeRightPane(){
        rightPanel = new JPanel(new MigLayout("insets 0 0 0 24, fill", "", ""));
        {
            JPanel frame = new JPanel(new MigLayout("insets 0, fill", "", ""));
            frame.setBorder(BorderFactory.createTitledBorder("患者検索"));
            searchPatientPane = new SearchPatient(patient -> {
                this.doStartPatient(patient);
                searchPatientPane.reset();
            });
            frame.add(searchPatientPane, "growx");
            rightPanel.add(frame, "top, growx, wrap");
        }
        rightScroll = new JScrollPane(rightPanel);
        rightScroll.setBorder(null);
        rightScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return rightScroll;
    }

    private void doStartPatient(PatientDTO patient){
        leftPanel.removeAll();
        leftPanel.setLayout(makeLeftPanelLayout());
        leftPanel.add(new PatientInfoPane(this, patient), "top, growx, wrap");
        leftPanel.repaint();
        leftPanel.revalidate();
    }
}
