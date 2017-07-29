package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.leftpane.DispRecords;
import jp.chang.myclinic.practice.leftpane.LeftPane;
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
        setLayout(new MigLayout("fill", "", ""));
        add(makeLeftPane(), "w 460, h 260, grow");
        add(makeRightPane(), "w 220, h 300, grow");
        pack();
    }

    private JComponent makeLeftPane(){
        leftPanel = new JPanel(new MigLayout("insets 0, fill", "", ""));
        leftPanel.setBorder(BorderFactory.createEmptyBorder());
        return leftPanel;
//        leftPanel = new JPanel(makeLeftPanelLayout());
//        leftScroll = new JScrollPane(leftPanel);
//        leftScroll.setBorder(null);
//        leftScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        return leftScroll;
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
        Service.api.listVisitFull(patient.patientId, 0)
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
//        Service.api.listVisitFull(patient.patientId, 0)
//                .thenAccept(page -> {
//                    EventQueue.invokeLater(() -> {
//                        leftPanel.removeAll();
//                        leftPanel.setLayout(makeLeftPanelLayout());
//                        DispRecords dispRecords = new DispRecords();
//                        leftPanel.add(new PatientInfoPane(this, patient), "growx, wrap");
//                        leftPanel.add(new PatientManip(), "top, wrap");
//                        leftPanel.add(new RecordsNav(0, page.totalPages,
//                                newPage -> onNavTrigger(patient.patientId, newPage, dispRecords)),
//                                "top, wrap");
//                        {
//                            JScrollPane scrollPane = new JScrollPane(dispRecords);
//                            scrollPane.setBorder(BorderFactory.createEmptyBorder());
//                            leftPanel.add(scrollPane, "grow, wrap");
//                            dispRecords.setVisits(page.visits);
//                        }
//                        leftPanel.add(new RecordsNav(0, page.totalPages,
//                                newPage -> onNavTrigger(patient.patientId, newPage, dispRecords)),
//                                "top, wrap");
//                        leftPanel.repaint();
//                        leftPanel.revalidate();
//                    });
//                })
//                .exceptionally(t -> {
//                    t.printStackTrace();
//                    alert(t.toString());
//                    return null;
//                });
    }

    private void onNavTrigger(int patientId, int page, DispRecords dispRecords){
        Service.api.listVisitFull(patientId, page)
                .thenAccept(visitPage -> {
                    dispRecords.setVisits(visitPage.visits);

                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
