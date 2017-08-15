package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.leftpane.DispRecords;
import jp.chang.myclinic.practice.leftpane.LeftPane;
import jp.chang.myclinic.practice.newvisitdialog.NewVisitDialog;
import jp.chang.myclinic.practice.rightpane.SearchPatient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class MainFrame extends JFrame {

    private JPanel leftPanel;
    private JScrollPane leftScroll;
    private JPanel rightPanel;
    private JScrollPane rightScroll;
    private SearchPatient searchPatientPane;

    MainFrame(){
        setTitle("診察");
        setupMenu();
        setLayout(new MigLayout("", "", ""));
        add(makeLeftPane(), "w 520!, h 520, grow");
        add(makeRightPane(), "w 220!, h 520, grow");
        pack();
    }

    private void setupMenu(){
        JMenuBar mbar = new JMenuBar();
        JMenu fileMenu = new JMenu("ファイル");
        {
            JMenuItem exitMenu = new JMenuItem("終了");
            exitMenu.addActionListener(event -> {
                dispose();
                System.exit(0);
            });
            fileMenu.add(exitMenu);
        }
        JMenu commandMenu = new JMenu("コマンド");
        {
            JMenuItem newVisitItem = new JMenuItem("診察受付");
            newVisitItem.addActionListener(event -> doNewVisit());
            commandMenu.add(newVisitItem);
        }
        mbar.add(fileMenu);
        mbar.add(commandMenu);
        setJMenuBar(mbar);
    }

    private JComponent makeLeftPane(){
        leftPanel = new JPanel(new MigLayout("insets 0, fill", "", ""));
        leftPanel.setBorder(BorderFactory.createEmptyBorder());
        return leftPanel;
    }

    private MigLayout makeLeftPanelLayout(){
        return new MigLayout("insets 0 0 0 24, fill", "", "[] [] [] [grow] []");
    }

    private JComponent makeRightPane(){
        rightPanel = new JPanel(new MigLayout("insets 0 0 0 24, fillx", "", ""));
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
        Service.api.listVisitFull2(patient.patientId, 0)
                .thenAccept(page -> {
                    EventQueue.invokeLater(() -> {
                        leftPanel.removeAll();
                        leftPanel.add(new LeftPane(patient, page), "grow");
                        leftPanel.repaint();
                        leftPanel.revalidate();
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private void onNavTrigger(int patientId, int page, DispRecords dispRecords){
        Service.api.listVisitFull2(patientId, page)
                .thenAccept(visitPage -> {
                    dispRecords.setVisits(visitPage.visits);

                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private void doNewVisit(){
        NewVisitDialog dialog = new NewVisitDialog();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
