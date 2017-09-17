package jp.chang.myclinic.practice.leftpane.conduct.adddrug;

import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.practice.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

class Disp extends JPanel {

    class ValidatedData {
        private IyakuhinMasterDTO master;
        private Double amount;
        private List<String> errors = new ArrayList<>();

        ConductDrugDTO toConductDrug(int conductId){
            if( master != null && amount != null && errors.size() == 0 ){
                ConductDrugDTO drug = new ConductDrugDTO();
                drug.iyakuhincode = master.iyakuhincode;
                drug.amount = amount;
                drug.conductId = conductId;
                return drug;
            } else {
                throw new RuntimeException(String.join("\n", errors));
            }
        }

        boolean hasError(){
            return errors.size() > 0;
        }

        List<String> getErrors(){
            return errors;
        }
    }

    private WrappedText nameText;
    private IyakuhinMasterDTO master;
    private JTextField amountField;
    private JLabel amountUnit;

    Disp(int width){
        setLayout(new MigLayout("insets 0", "[]4[grow]", ""));
        JLabel nameLabel = new JLabel("名称：");
        nameText = new WrappedText(width - nameLabel.getPreferredSize().width - 4);
        JLabel amountLabel = new JLabel("用量：");
        amountField = new JTextField(4);
        amountUnit = new JLabel("");
        add(nameLabel);
        add(nameText, "wrap");
        add(amountLabel);
        add(amountLabel);
        add(amountField, "split 2");
        add(amountUnit);
    }

    Disp(int width, IyakuhinMasterDTO master) {
        this(width);
        setMaster(master);
    }

    void setMaster(IyakuhinMasterDTO master){
        this.master = master;
        nameText.setText(master.name);
        amountUnit.setText(master.unit);
    }

    ValidatedData getData(){
        ValidatedData data = new ValidatedData();
        data.master = master;
        if( data.master == null ){
            data.errors.add("医薬品が設定されていません。");
        }
        try {
            data.amount = Double.parseDouble(amountField.getText());
        } catch(NumberFormatException ex){
            data.errors.add("用量の設定が不適切です。");
        }
        return data;
    }
}
