package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrugArea extends JPanel {

    public interface Callback {
        void onCopyAll(int targetVisitId, List<Integer> drugIds);
    }

    private DrugListDisp drugListDisp;

    public DrugArea(List<DrugFullDTO> drugs, VisitDTO visit, int currentVisitId, int tempVisitId, Callback callback){
        DrugMenu drugMenu = new DrugMenu(visit, currentVisitId, tempVisitId);
        this.drugListDisp = new DrugListDisp(drugs);
        drugMenu.setCallback(new DrugMenu.Callback(){
            @Override
            public void onNewDrug(DrugDTO drug) {
                Service.api.getDrugFull(drug.drugId)
                        .thenAccept(drugFull -> {
                            EventQueue.invokeLater(() -> drugListDisp.addDrug(drugFull));
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            }

            @Override
            public void onDrugsCopied(int targetVisitId, List<Integer> drugIds) {
                callback.onCopyAll(targetVisitId, drugIds);
            }

            @Override
            public void onDrugsModified(List<DrugFullDTO> modifiedDrugs) {
                drugListDisp.update(modifiedDrugs);
            }
        });
        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        add(drugMenu, "growx, wrap");
        add(drugListDisp, "growx, wrap");
    }

    public void appendDrugs(List<DrugFullDTO> drugs){
        drugListDisp.addDrugs(drugs);
        drugListDisp.revalidate();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
