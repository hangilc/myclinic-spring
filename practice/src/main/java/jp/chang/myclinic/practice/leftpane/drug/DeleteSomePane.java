package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class DeleteSomePane extends JPanel {
    interface Callback {
        default void onEnter(List<DrugFullDTO> selected){}
        default void onCancel(){}
    }

    private List<Item> items = new ArrayList<>();
    private Callback callback = new Callback(){};

    DeleteSomePane(List<DrugFullDTO> candidates){
        super(new MigLayout("insets 0", "[] [grow]", ""));
        candidates.forEach(drug -> {
            JCheckBox checkbox = new JCheckBox("");
            JEditorPane rep = new JEditorPane("text/plain", DrugUtil.drugRep(drug));
            rep.setBackground(getBackground());
            add(checkbox);
            add(rep, "growx, wrap");
            items.add(new Item(drug, checkbox));
        });
        add(makeBatchSelectBox(), "span, wrap");
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

    private JComponent makeBatchSelectBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        Link selectAllLink = new Link("全部選択");
        selectAllLink.setCallback(event -> batchSelect(true));
        Link unselectAllLink = new Link("全部解除");
        unselectAllLink.setCallback(event -> batchSelect(false));
        panel.add(selectAllLink);
        panel.add(unselectAllLink);
        return panel;
    }

    private void batchSelect(boolean select){
        items.forEach(item -> item.checkbox.setSelected(select));
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
        List<DrugFullDTO> srcDrugs = items.stream()
                .filter(item -> item.checkbox.isSelected())
                .map(item -> item.drug)
                .collect(Collectors.toList());
        callback.onEnter(srcDrugs);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
