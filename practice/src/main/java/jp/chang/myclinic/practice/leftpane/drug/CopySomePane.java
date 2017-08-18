package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class CopySomePane extends JPanel {

    interface Callback {
        default void onEnter(List<DrugFullDTO> selected){}
        default void onCancel(){}
    }

    private JTextField daysField = new JTextField(3);
    private List<Item> items = new ArrayList<>();
    private Callback callback = new Callback(){};

    CopySomePane(List<DrugFullDTO> candidates){
        super(new MigLayout("insets 0", "[] [grow]", ""));
        candidates.forEach(drug -> {
            JCheckBox checkbox = new JCheckBox("");
            JEditorPane rep = new JEditorPane("text/plain", DrugUtil.drugRep(drug));
            rep.setBackground(getBackground());
            add(checkbox);
            add(rep, "growx, wrap");
            items.add(new Item(drug, checkbox));
        });
        add(makeDaysBox(), "span, wrap");
        add(makeCommandBox(), "span");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private class Item {
        DrugFullDTO drug;
        JCheckBox checkbox;

        Item(DrugFullDTO drug, JCheckBox checkbox){
            this.drug = drug;
            this.checkbox = checkbox;
        }
    }

    private JComponent makeDaysBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        add(new JLabel("日数"));
        add(daysField);
        return panel;
    }

    private JComponent makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> doEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        panel.add(enterButton);
        panel.add(cancelButton);
        return panel;
    }

    private void doEnter(){
        String daysInput = daysField.getText();
        int gaiyouCode = DrugCategory.Gaiyou.getCode();
        try {
            final int days = daysInput.isEmpty() ? 0 : Integer.parseInt(daysInput);
            List<DrugFullDTO> srcDrugs = items.stream()
                    .filter(item -> item.checkbox.isSelected())
                    .map(item -> {
                        if( days > 0 && item.drug.drug.category != gaiyouCode ){
                            DrugDTO newDrug = DrugDTO.copy(item.drug.drug);
                            newDrug.days = days;
                            DrugFullDTO newFullDrug = DrugFullDTO.copy(item.drug);
                            newFullDrug.drug = newDrug;
                            return newFullDrug;
                        } else {
                            return item.drug;
                        }
                    })
                    .collect(Collectors.toList());
            callback.onEnter(srcDrugs);
        } catch(NumberFormatException ex){
            alert("日数の指定が不適切です。");
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
