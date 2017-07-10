package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PharmaDrugDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class PharmaDrugEditor extends JPanel {

    private JTextArea descInput;
    private JTextArea sideeffectInput;

    public PharmaDrugEditor(String description, String sideeffect){
        setLayout(new MigLayout("insets 0", "", ""));
        add(new JLabel("説明"), "wrap");
        descInput = new JTextArea(6, 30);
        add(descInput, "wrap");
        add(new JLabel("副作用"), "wrap");
        sideeffectInput = new JTextArea(6, 30);
        add(sideeffectInput);
        descInput.setText(description);
        sideeffectInput.setText(sideeffect);
    }

    public PharmaDrugEditor(PharmaDrugDTO pharmaDrug){
        this(pharmaDrug.description, pharmaDrug.sideeffect);
     }

     public PharmaDrugEditor(){
        this("", "");
     }

    public String getDescription(){
        return descInput.getText();
    }

    public String getSideEffect(){
        return sideeffectInput.getText();
    }

}
