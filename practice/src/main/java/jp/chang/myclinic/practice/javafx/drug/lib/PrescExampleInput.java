package jp.chang.myclinic.practice.javafx.drug.lib;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.validator.Validated;
import jp.chang.myclinic.util.validator.dto.PrescExampleValidator;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrescExampleInput extends DrugInputBase {

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

    @Override
    public void setPrescExample(PrescExampleFullDTO exampleFull){
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

    @Override
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

    @Override
    void setComment(String comment){
        commentField.setText(comment);
    }

    @Override
    void categoryChangeHook(DrugCategory prevCategory, DrugCategory newCategory) {
        // nop
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

    public Validated<PrescExampleDTO> getValidatedToEnter(){
        return new PrescExampleValidator()
                .setValidatedPrescExampleId(0)
                .validateIyakuhincode(getIyakuhincode())
                .validateAmount(getAmount())
                .validateUsage(getUsage())
                .validateCategory(getCategory().getCode())
                .validateComment(getComment())
                .validateDays(getDays())
                .validateMasterValidFrom(masterValidFrom)
                .validate();
    }

    public Validated<PrescExampleDTO> getValidatedToUpdate(){
        return new PrescExampleValidator()
                .validatePrescExampleId(getPrescExampleId())
                .validateIyakuhincode(getIyakuhincode())
                .validateAmount(getAmount())
                .validateUsage(getUsage())
                .validateCategory(getCategory().getCode())
                .validateComment(getComment())
                .validateDays(getDays())
                .validateMasterValidFrom(masterValidFrom)
                .validate();
    }

}
