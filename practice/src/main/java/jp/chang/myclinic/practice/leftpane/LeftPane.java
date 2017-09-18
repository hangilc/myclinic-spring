package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.MainContext;
import jp.chang.myclinic.practice.MainExecContext;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.cashierdialog.CashierDialog;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeftPane extends JPanel implements LeftPaneContext {

    private MainExecContext mainExecContext;
    private PatientInfoPane patientInfoPane;
    private RecordsNav topNav;
    private DispRecords dispRecords;
    private JScrollPane dispScroll;
    private RecordsNav botNav;

    public LeftPane(MainExecContext mainExecContext){
        setLayout(new MigLayout("insets 0", "[grow]", "[] [] [grow] []"));
        this.mainExecContext = mainExecContext;
        EventQueue.invokeLater(this::setupComponents);
        setVisible(false);
    }

    private void setupComponents(){
        int width = getSize().width;
        this.patientInfoPane = new PatientInfoPane(width);
        PatientManip patientManip = new PatientManip();
        bindPatientManip(patientManip);
        this.topNav = makeNav();
        this.botNav = makeNav();
        dispRecords = new DispRecords(width, mainExecContext);
        dispScroll = new JScrollPane(dispRecords);
        dispScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        dispScroll.setBorder(null);
        dispScroll.getVerticalScrollBar().setUnitIncrement(16);
        dispScroll.getVerticalScrollBar().setBlockIncrement(160);
        add(patientInfoPane, "wrap");
        add(patientManip, "wrap");
        add(topNav, "wrap");
        add(dispScroll, "grow, wrap");
        add(botNav, "");
        revalidate();
    }

    private void bindPatientManip(PatientManip patientManip){
        LeftPane self = this;
        patientManip.setCallback(new PatientManip.Callback() {
            @Override
            public void onCashier() {
                MainContext mainContext = MainContext.get(self);
                int visitId = mainContext.getCurrentVisitId();
                if( visitId > 0 ){
                    doCashier(visitId);
                    Service.api.getMeisai(visitId)
                            .thenAccept(meisai -> EventQueue.invokeLater(() ->{

                            }))
                            .exceptionally(t -> {
                                t.printStackTrace();
                                EventQueue.invokeLater(() -> {
                                    alert(t.toString());
                                });
                                return null;
                            });
                }
            }

            @Override
            public void onFinishPatient() {
                MainContext mainContext = MainContext.get(self);
                if( mainContext.getCurrentVisitId() == 0 ){
                    mainContext.endBrowse();
                } else {
                    mainContext.suspendExam(() -> {});
                }
            }
        });
    }

    private static class CashierData {
        MeisaiDTO meisai;
        PatientDTO patient;
        VisitDTO visit;
    }

    private void doCashier(int visitId){
        CashierData data = new CashierData();
        LeftPane self = this;
        Service.api.getMeisai(visitId)
                .thenCompose(meisai -> {
                    data.meisai = meisai;
                    return Service.api.getVisit(visitId);
                })
                .thenCompose(visit -> {
                    data.visit = visit;
                    return Service.api.getPatient(visit.patientId);
                })
                .thenAccept(patient -> {
                    data.patient = patient;
                    Window owner = SwingUtilities.getWindowAncestor(this);
                    CashierDialog dialog = new CashierDialog(owner, data.meisai, data.patient, data.visit);
                    dialog.setCallback(new CashierDialog.Callback() {
                        @Override
                        public void onEnter(int charge) {
                            MainContext.get(self).endExam(charge, () -> dialog.dispose());
                        }

                        @Override
                        public void onCancel() {
                            dialog.dispose();
                        }
                    });
                    dialog.setLocationByPlatform(true);
                    dialog.setVisible(true);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    public void reset(){
        dispRecords.clear();
        setVisible(false);
        repaint();
        revalidate();
    }

    public void start(VisitFull2PageDTO page){
        PatientDTO patient = mainExecContext.getCurrentPatient();
        patientInfoPane.setPatient(patient);
        topNav.start(patient.patientId, page.totalPages);
        botNav.start(patient.patientId, page.totalPages);
        dispRecords.setVisits(page.visits);
        EventQueue.invokeLater(() -> {
            dispScroll.getVerticalScrollBar().setValue(0);
            setVisible(true);
            repaint();
            revalidate();
        });
    }

    private RecordsNav makeNav(){
        return new RecordsNav(){
            @Override
            void changePage(VisitFull2PageDTO visitFullPage, int page) {
                dispRecords.setVisits(visitFullPage.visits);
                topNav.set(page, visitFullPage.totalPages);
                botNav.set(page, visitFullPage.totalPages);
            }
        };
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void onDrugsEntered(int visitId, List<DrugFullDTO> drugs) {
        dispRecords.appendDrugs(visitId, drugs);
    }

    @Override
    public void onShinryouEntered(int visitId, List<ShinryouFullDTO> copied, Runnable uiCallback) {
        dispRecords.appendShinryou(visitId, copied, uiCallback);
    }

    @Override
    public void onConductEntered(int visitId, List<ConductFullDTO> entered, Runnable uiCallback) {
        dispRecords.appendConduct(visitId, entered, uiCallback);
    }

}
