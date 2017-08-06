package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DrugNew extends JPanel {
    interface Callback {
        default void onEnter(DrugDTO drug){};
        default void onClose(){};
    }

    private DrugInfoNew drugInfoPane = new DrugInfoNew();
    private Callback callback = new Callback(){};

    DrugNew(VisitDTO visit){
        DrugSearch drugSearch = new DrugSearch(visit.patientId, visit.visitedAt.substring(0, 10),
                this::doOnDrugSelected);
        setLayout(new MigLayout("", "[grow]", ""));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(new JLabel("新規処方の入力"), "growx, wrap");
        add(drugInfoPane, "growx, wrap");
        add(makeCommandBox(visit), "growx, wrap");
        add(drugSearch, "growx");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private JComponent makeCommandBox(VisitDTO visit){
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> callback.onEnter(drugInfoPane.getDrug()));
        JButton closeButton = new JButton("閉じる");
        closeButton.addActionListener(event -> callback.onClose());
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
                    drugInfoPane.usageField.setText(selectedDrug.getUsage());
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
