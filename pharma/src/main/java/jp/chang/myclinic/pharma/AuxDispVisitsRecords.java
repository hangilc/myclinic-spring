package jp.chang.myclinic.pharma;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;

public class AuxDispVisitsRecords extends JPanel {

    public AuxDispVisitsRecords(){
        setLayout(new MigLayout("", "", ""));
        add(new JLabel("Records"));
    }

    public void showVisits(List<Integer> visitIds){
        Service.api.listVisitTextDrug(new HashSet<Integer>(visitIds))
                .thenAccept(records -> {
                    System.out.println(records);
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
