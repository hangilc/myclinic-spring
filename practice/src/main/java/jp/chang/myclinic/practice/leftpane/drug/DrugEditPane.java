package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DrugEditPane extends JPanel {

    interface Callback {
        default void onUpdated(DrugFullDTO drugFull){};
        default void onClose(){};
    }

    private DrugInfoEdit drugInfoPane;
    private Callback callback = new Callback(){};

    DrugEditPane(DrugFullDTO drugFull, VisitDTO visit){
        drugInfoPane = new DrugInfoEdit(drugFull);
        DrugSearch drugSearch = new DrugSearch(visit.patientId, visit.visitedAt.substring(0, 10), this::doOnDrugSelected);
        setLayout(new MigLayout("", "[grow]", ""));
        add(drugInfoPane, "growx, wrap");
        add(makeCommandBox(drugFull, visit), "growx, wrap");
        add(drugSearch, "growx");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doOnDrugSelected(DrugSearch.SearchResult selectedDrug){
        selectedDrug.resolveMaster()
                .thenAccept(master -> EventQueue.invokeLater(() -> {
                    DrugCategory category = selectedDrug.getCategory();
                    drugInfoPane.setMaster(master);
                    drugInfoPane.setCategory(category);
                    drugInfoPane.amountField.setText(selectedDrug.getAmount().map(NumberUtil::formatNumber).orElse(""));
                    if (category != DrugCategory.Gaiyou) {
                        String t = selectedDrug.getDays().map(Object::toString).orElse("");
                        drugInfoPane.daysField.setText(t);
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

    private JComponent makeCommandBox(DrugFullDTO drugFull, VisitDTO visit){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> {
            DrugDTO newDrug = drugInfoPane.getDrug();
            newDrug.drugId = drugFull.drug.drugId;
            newDrug.visitId = visit.visitId;
            newDrug.prescribed = drugFull.drug.prescribed;
            newDrug.shuukeisaki = drugFull.drug.shuukeisaki;
            Service.api.updateDrug(newDrug)
                    .thenCompose(ok -> Service.api.getDrugFull(newDrug.drugId))
                    .thenAccept(newDrugFull -> EventQueue.invokeLater(() -> callback.onUpdated(newDrugFull)))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onClose());
        Link deleteLink = new Link("削除");
        panel.add(enterButton);
        panel.add(cancelButton);
        panel.add(deleteLink);
        return panel;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
