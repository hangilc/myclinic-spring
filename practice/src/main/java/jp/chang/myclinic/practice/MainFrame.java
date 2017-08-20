package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.leftpane.LeftPaneWrapper;
import jp.chang.myclinic.practice.newvisitdialog.NewVisitDialog;
import jp.chang.myclinic.practice.rightpane.RightPaneWrapper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

class MainFrame extends JFrame {

    private LeftPaneWrapper leftPaneWrapper;
    private RightPaneWrapper rightPaneWrapper;
    private PatientDTO currentPatient;
    private VisitDTO currentVisit;
    private int tempVisitId;

    MainFrame(){
        setTitle("診察");
        setupMenu();
        setLayout(new MigLayout("", "", ""));
        MainExecContext ctx = makeMainExecContext();
        leftPaneWrapper = new LeftPaneWrapper(ctx);
        JScrollPane leftScroll = new JScrollPane(leftPaneWrapper);
        leftScroll.setBorder(null);
        leftScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        rightPaneWrapper = new RightPaneWrapper(ctx);
        JScrollPane rightScroll = new JScrollPane(rightPaneWrapper);
        rightScroll.setBorder(null);
        add(leftScroll, "w 580!, h 520, grow");
        add(rightScroll, "w 220!, h 520, grow");
        pack();
    }

    private MainExecContext makeMainExecContext(){
        return new MainExecContext(){
            @Override
            public CompletableFuture<Boolean> startExam(VisitDTO visit, PatientDTO patient) {
                return doStartExam(visit, patient);
            }

            @Override
            public PatientDTO getCurrentPatient() {
                return currentPatient;
            }
        };
    }

    private CompletableFuture<Boolean> doStartExam(VisitDTO visit, PatientDTO patient) {
        return suspendCurrent()
                .thenApply(ok -> {
                    currentPatient = patient;
                    currentVisit = visit;
                    tempVisitId = 0;
                    leftPaneWrapper.start();
                    return true;
                });
    }

    private CompletableFuture<Boolean> suspendCurrent(){
        if( currentVisit != null ){
            return Service.api.suspendExam(currentVisit.visitId);
        } else {
            return CompletableFuture.completedFuture(true);
        }

    }

//    private CompletableFuture<Boolean> closeCurrent(){
//        CompletableFuture<Boolean> suspendExam;
//        if( currentVisit != null ){
//            suspendExam = Service.api.suspendExam(currentVisit.visitId);
//        } else {
//            suspendExam = CompletableFuture.completedFuture(true);
//        }
//        return suspendExam.thenApply(ok -> EventQueue.invokeLater(() ->{
//            leftPaneWrapper.reset();
//            currentPatient = null;
//            currentVisit = null;
//            tempVisitId = 0;
//            return true;
//        }));
//    }

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

    private void doNewVisit(){
        NewVisitDialog dialog = new NewVisitDialog();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
