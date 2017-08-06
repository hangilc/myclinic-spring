package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DrugNew extends JPanel {

    private DrugInfoNew drugInfoPane = new DrugInfoNew();

    DrugNew(int patientId){
        DrugSearch drugSearch = new DrugSearch(patientId, this::doOnDrugSelected);
        setLayout(new MigLayout("", "[grow]", ""));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(new JLabel("新規処方の入力"), "growx, wrap");
        add(drugInfoPane, "growx, wrap");
        add(makeCommandBox(), "growx, wrap");
        add(drugSearch, "growx");
    }

    private JComponent makeCommandBox(){
        JButton enterButton = new JButton("入力");
        JButton closeButton = new JButton("閉じる");
        Link clearLink = new Link("クリア");
        JPanel box = new JPanel(new MigLayout("", "", ""));
        box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        box.add(enterButton);
        box.add(closeButton);
        box.add(clearLink);
        return box;
    }

    private void doOnDrugSelected(DrugSearch.SearchResult selectedDrug){
        selectedDrug.resolveMaster()
                .thenAccept(master -> {
                    DrugCategory category = selectedDrug.getCategory();
                    drugInfoPane.setMaster(master);
                    drugInfoPane.setCategory(category);
                    drugInfoPane.amountField.setText(selectedDrug.getAmount().map(NumberUtil::formatNumber).orElse(""));
                    if( category != DrugCategory.Gaiyou ){
                        String t = selectedDrug.getDays().map(Object::toString).orElse("");
                        drugInfoPane.daysField.setText(t);
                    }
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });

    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
