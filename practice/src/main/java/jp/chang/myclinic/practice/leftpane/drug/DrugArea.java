package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.MainExecContext;

import javax.swing.*;
import java.util.List;

public class DrugArea extends JPanel {

    public interface Callback {
        default void onCopyAll(int targetVisitId, List<DrugFullDTO> enteredDrugs){}
    }

    private DrugListDisp drugListDisp;
    private Callback callback;

    public DrugArea(List<DrugFullDTO> drugs, VisitDTO visit, int currentVisitId, int tempVisitId, Callback callback){
//        DrugMenu1 drugMenu = new DrugMenu1(visit, currentVisitId, tempVisitId);
//        this.drugListDisp = new DrugListDisp(drugs, visit);
//        drugMenu.setCallback(new DrugMenu1.Callback(){
//            @Override
//            public void onNewDrug(DrugDTO drug) {
//                Service.api.getDrugFull(drug.drugId)
//                        .thenAccept(drugFull -> {
//                            EventQueue.invokeLater(() -> drugListDisp.addDrug(drugFull));
//                        })
//                        .exceptionally(t -> {
//                            t.printStackTrace();
//                            EventQueue.invokeLater(() -> {
//                                alert(t.toString());
//                            });
//                            return null;
//                        });
//            }
//
//            @Override
//            public void onDrugsCopied(int targetVisitId, List<Integer> drugIds) {
//                callback.onCopyAll(targetVisitId, drugIds);
//            }
//
//            @Override
//            public void onDrugsModified(List<DrugFullDTO> modifiedDrugs) {
//                drugListDisp.update(modifiedDrugs);
//            }
//
//            @Override
//            public void onDrugsDeleted(List<Integer> drugIds) {
//                drugListDisp.deleteDrugs(drugIds);
//            }
//        });
//        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
//        add(drugMenu, "growx, wrap");
//        add(drugListDisp, "growx, wrap");
    }

    public DrugArea(List<DrugFullDTO> drugs, VisitDTO visit, int width, MainExecContext mainExecContext){
        int currentVisitId = mainExecContext.getCurrentVisitId();
        int tempVisitId = mainExecContext.getTempVisitId();
        DrugMenu1 drugMenu1 = new DrugMenu1(visit, currentVisitId, tempVisitId);
        bindDrugMenu(drugMenu1);
        this.drugListDisp = new DrugListDisp(drugs, visit, width);
        //setLayout(new MigLayout("insets 0, gapy 0", String.format("[%dpx!]", width), ""));
        setLayout(new FixedWidthLayout(width));
        add(drugMenu1, "wrap");
        add(drugListDisp);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void bindDrugMenu(DrugMenu1 drugMenu1){
        drugMenu1.setCallback(new DrugMenu1.Callback(){
            @Override
            public void onNewDrug(DrugFullDTO drugFull) {
                drugListDisp.addDrug(drugFull);
            }

            @Override
            public void onDrugsCopied(int targetVisitId, List<DrugFullDTO> enteredDrugs) {
                callback.onCopyAll(targetVisitId, enteredDrugs);
            }

            @Override
            public void onDrugsModified(List<DrugFullDTO> modifiedDrugs) {

            }

            @Override
            public void onDrugsDeleted(List<Integer> drugIds) {

            }
        });
    }

    public void appendDrugs(List<DrugFullDTO> drugs){
        drugListDisp.addDrugs(drugs);
        drugListDisp.revalidate();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
