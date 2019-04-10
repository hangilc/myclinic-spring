package jp.chang.myclinic.practice.javafx.drug.lib2;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrugEditInput extends DrugInputBase {

    private Label tekiyouLabel = new Label();
    private HBox tekiyouRow;
    private CheckBox allFixedCheck = new CheckBox("用量・用法・日数をそのままに");

    public DrugEditInput(DrugFullDTO drug, DrugAttrDTO attr) {
        super();
        addRow(allFixedCheck);
        this.tekiyouRow = addRowBeforeCategory(new Label("摘要："), tekiyouLabel);
        setDrug(drug);
        setTekiyou((attr != null && attr.tekiyou != null) ? attr.tekiyou : "");
    }

    @Override
    void categoryChangeHook(DrugCategory prevCategory, DrugCategory newCategory) {
        // nop
    }

    @Override
    void setDrugData(String amount, String usage, String days) {
        if( allFixedCheck.isSelected() ){
            // nop
        } else {
            setAmount(amount);
            setUsage(usage);
            setDays(days);
        }
    }

    private void setTekiyou(String tekiyou){
        this.tekiyouLabel.setText(tekiyou);
        boolean visible = tekiyou != null && !tekiyou.isEmpty();
        tekiyouRow.setManaged(visible);
        tekiyouRow.setVisible(visible);
    }
}
