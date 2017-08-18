package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitFull2PageDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.practice.leftpane.LeftPane;
import jp.chang.myclinic.practice.newvisitdialog.NewVisitDialog;
import jp.chang.myclinic.practice.rightpane.SearchPatient;
import jp.chang.myclinic.practice.rightpane.selectvisit.SelectVisit;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

class MainFrame extends JFrame {

    private JPanel leftPanel;
    private JScrollPane leftScroll;
    private JPanel rightPanel;
    private JScrollPane rightScroll;
    private SearchPatient searchPatientPane;
    private PatientDTO currentPatient;
    private VisitDTO currentVisit;
    private int tempVisitId;

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

    private JComponent makeRightPane(){
        SelectVisit selectVisit = new SelectVisit();
        selectVisit.setCallback(new SelectVisit.Callback(){
            @Override
            public void onSelect(WqueueFullDTO wqueue) {
                doStartExam(wqueue.patient, wqueue.visit);
            }
        });
        rightPanel = new JPanel(new MigLayout("insets 0 0 0 24, fillx", "[grow]", ""));
        rightPanel.add(selectVisit, "growx, wrap");
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
        closeCurrentPatient()
                .thenCompose(result -> Service.api.listVisitFull2(patient.patientId, 0))
                .thenAccept(page -> {
                    EventQueue.invokeLater(() -> {
                        currentPatient = patient;
                        leftPanel.add(createLeftPaneContent(patient, page), "grow");
                        leftPanel.repaint();
                        leftPanel.revalidate();
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private LeftPane createLeftPaneContent(PatientDTO patient, VisitFull2PageDTO page){
        int currentVisitId = currentVisit == null ? 0 : currentVisit.visitId;
        return new LeftPane(patient, page, currentVisitId, tempVisitId, new LeftPane.Callback(){
            @Override
            public void onFinishPatient() {
                closeCurrentPatient();
            }
        });
    }

    private void doNewVisit(){
        NewVisitDialog dialog = new NewVisitDialog();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

    private void doStartExam(PatientDTO patient, VisitDTO visit){
        closeCurrentPatient()
                .thenCompose(result -> Service.api.startExam(visit.visitId))
                .thenCompose(result -> Service.api.listVisitFull2(patient.patientId, 0))
                .thenAccept(page -> EventQueue.invokeLater(() ->{
                    currentPatient = patient;
                    currentVisit = visit;
                    leftPanel.add(createLeftPaneContent(patient, page), "grow");
                    leftPanel.repaint();
                    leftPanel.revalidate();
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private CompletableFuture<Boolean> closeCurrentPatient(){
        int visitIdSave = (currentVisit != null ) ? currentVisit.visitId : 0;
        tempVisitId = 0;
        currentPatient = null;
        currentVisit = null;
        clearCurrentPatient();
        if( visitIdSave > 0 ){
            return Service.api.suspendExam(visitIdSave);
        } else {
            return CompletableFuture.completedFuture(true);
        }
    }

    private void clearCurrentPatient(){
        leftPanel.removeAll();
        leftPanel.repaint();
        leftPanel.revalidate();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
