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

    public DrugArea(List<DrugFullDTO> drugs, VisitDTO visit){
        DrugMenu drugMenu = new DrugMenu(visit);
        DrugListDisp drugListDisp = new DrugListDisp(drugs);
        drugMenu.setCallback(new DrugMenu.Callback(){
            @Override
            public void onNewDrug(DrugDTO drug) {
                Service.api.getDrugFull(drug.drugId)
                        .thenAccept(drugListDisp::addDrug)
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            }
        });
        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        add(drugMenu, "growx, wrap");
        add(drugListDisp, "growx, wrap");
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
