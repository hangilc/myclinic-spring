package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.leftpane.LeftPaneWrapper;
import jp.chang.myclinic.practice.newvisitdialog.NewVisitDialog;
import jp.chang.myclinic.practice.rightpane.RightPaneWrapper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

class MainFrame extends JFrame implements MainContext {

    private LeftPaneWrapper leftPaneWrapper;
    private RightPaneWrapper rightPaneWrapper;
    private PatientDTO currentPatient;
    private VisitDTO currentVisit;
    private int tempVisitId;

    MainFrame(){
        setTitle("診察");
        setupMenu();
        setLayout(new MigLayout("", "", "[grow]"));
        MainExecContext ctx = makeMainExecContext();
        leftPaneWrapper = new LeftPaneWrapper(ctx);
        rightPaneWrapper = new RightPaneWrapper(ctx);
        JScrollPane rightScroll = new JScrollPane(rightPaneWrapper);
        rightScroll.setBorder(null);
        add(leftPaneWrapper, "w 580!, h 520, growy");
        add(rightScroll, "w 220!, h 520, growy");
        pack();
    }

    @Override
    public void startExam(PatientDTO patient, VisitDTO visit, Runnable edtCallback) {
        suspendCurrentExam()
                .thenCompose(res -> Service.api.listVisitFull2(patient.patientId, 0))
                .thenAccept(page -> EventQueue.invokeLater(() -> {
                    System.out.println(page);
                    currentPatient = patient;
                    currentVisit = visit;
                    tempVisitId = 0;
                    leftPaneWrapper.start(page);
                    edtCallback.run();
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private CompletableFuture<Boolean> suspendCurrentExam(){
        CompletableFuture<Boolean> cf;
        if( currentVisit != null ){
            cf = Service.api.suspendExam(currentVisit.visitId);
        } else {
            cf = CompletableFuture.completedFuture(true);
        }
        return cf.thenApply(result -> {
            EventQueue.invokeLater(() -> {
                currentPatient = null;
                currentVisit = null;
                tempVisitId = 0;
                //leftPaneWrapper.reset();
            });
            return true;
        });
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

            @Override
            public VisitDTO getCurrentVisit() {
                return currentVisit;
            }

            @Override
            public int getTempVisitId() {
                return tempVisitId;
            }
        };
    }

    private CompletableFuture<Boolean> doStartExam(VisitDTO visit, PatientDTO patient) {
        return suspendCurrent()
                .thenCompose(ok -> {
                    currentPatient = patient;
                    currentVisit = visit;
                    tempVisitId = 0;
                    return Service.api.listVisitFull2(patient.patientId, 0);
                })
                .thenApply(visits -> {
                    leftPaneWrapper.start(visits);
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
