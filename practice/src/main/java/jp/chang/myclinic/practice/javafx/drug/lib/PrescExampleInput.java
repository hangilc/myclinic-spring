package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrescExampleInput extends InputBase {

    private static Logger logger = LoggerFactory.getLogger(PrescExampleInput.class);
    private int prescExampleId;
    private TextField commentField = new TextField();
    private String masterValidFrom;

    public PrescExampleInput() {
        addRowBeforeCategory(new Label("注釈："), commentField);
    }

    @Override
    public void setMaster(IyakuhinMasterDTO master) {
        super.setMaster(master);
        this.masterValidFrom = master.validFrom;
    }

    public void setExample(PrescExampleFullDTO exampleFull){
        setMaster(exampleFull.master);
        this.prescExampleId = exampleFull.prescExample.prescExampleId;
        PrescExampleDTO example = exampleFull.prescExample;
        try {
            double amount = Double.parseDouble(example.amount);
            setAmount(amount);
        } catch(NumberFormatException ex){
            logger.error("Invalid amount: " + example.amount);
            clearAmount();
        }
        setUsage(example.usage);
        setCategory(DrugCategory.fromCode(example.category));
        setDays(example.days);
        setComment(exampleFull.prescExample.comment);
    }

    public void setDrug(DrugFullDTO drugFull){
        setMaster(drugFull.master);
        DrugDTO drug = drugFull.drug;
        setAmount(drug.amount);
        setUsage(drug.usage);
        setCategory(DrugCategory.fromCode(drug.category));
        setDays(drug.days);
    }

    public int getPrescExampleId() {
        return prescExampleId;
    }

    private void setComment(String comment){
        commentField.setText(comment);
    }

    private String getComment(){
        return commentField.getText();
    }

    private void clearComment(){
        commentField.setText("");
    }

    public void clear(){
        clearMaster();
        clearAmount();
        clearUsage();
        clearDays();
        clearComment();
    }

    public PrescExampleDTO createPrescExample(){
        PrescExampleDTO ex = new PrescExampleDTO();
        ex.prescExampleId = prescExampleId;
        ex.iyakuhincode = getIyakuhincode();
        ex.masterValidFrom = masterValidFrom;
        if( ex.masterValidFrom == null ){
            throw new RuntimeException("masterValidFrom is null.");
        }
        if (ex.iyakuhincode == 0) {
            GuiUtil.alertError("医薬品が設定されていません。");
            return null;
        }
        ex.amount = getAmount();
        try {
            double value = Double.parseDouble(ex.amount);
            if (!(value > 0)) {
                GuiUtil.alertError("用量の値が正でありません。");
                return null;
            }
        } catch (NumberFormatException e) {
            GuiUtil.alertError("用量の入力が不適切です。");
            return null;
        }
        ex.usage = getUsage();
        DrugCategory category = getCategory();
        ex.category = category.getCode();
        if (category == DrugCategory.Gaiyou) {
            ex.days = 1;
        } else {
            try {
                ex.days = Integer.parseInt(getDays());
                if (!(ex.days > 0)) {
                    GuiUtil.alertError("日数の値が正の整数でありません。");
                    return null;
                }
            } catch (NumberFormatException e) {
                GuiUtil.alertError("日数の入力が不敵津です。");
                return null;
            }
        }
        ex.comment = getComment();
        return ex;
    }

}
