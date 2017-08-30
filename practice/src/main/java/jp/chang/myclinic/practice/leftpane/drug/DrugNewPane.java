package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DrugNewPane extends JPanel {

    interface Callback {
        default void onEnter(DrugFullDTO enteredDrug){}
        default void onClose(){}
    }

    private DrugInfoNew drugInfoPane;
    private Callback callback = new Callback(){};

    DrugNewPane(VisitDTO visit){
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        drugInfoPane = new DrugInfoNew();
        String atDate = visit.visitedAt.substring(0, 10);
        DrugSearch drugSearch = new DrugSearch(visit.patientId, atDate, this::doOnDrugSelected);
        add(drugInfoPane, "growx, wrap");
        add(makeCommandBox(visit.visitId), "growx, wrap");
        add(drugSearch, "growx, wrap");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private JComponent makeCommandBox(int visitId){
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> {
            DrugDTO newDrug = drugInfoPane.getDrug();
            newDrug.visitId = visitId;
            DrugLib.enterDrug(newDrug)
                    .thenAccept(callback::onEnter)
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
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
                .thenAccept(master -> EventQueue.invokeLater(() -> {
                    DrugCategory category = selectedDrug.getCategory();
                    drugInfoPane.setMaster(master);
                    drugInfoPane.setCategory(category);
                    drugInfoPane.amountField.setText(selectedDrug.getAmount().map(NumberUtil::formatNumber).orElse(""));
                    if( category != DrugCategory.Gaiyou ){
                        if( !drugInfoPane.isDaysFixed() || drugInfoPane.isDaysFieldEmpty() ){
                            String t = selectedDrug.getDays().map(Object::toString).orElse("");
                            drugInfoPane.daysField.setText(t);
                        }
                    }
                    drugInfoPane.usageField.setText(selectedDrug.getUsage());
                }))
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
