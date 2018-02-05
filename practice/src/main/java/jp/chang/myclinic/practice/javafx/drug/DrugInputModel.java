package jp.chang.myclinic.practice.javafx.drug;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DrugInputModel {
    private int iyakuhincode;
    private StringProperty drugName = new SimpleStringProperty("");
    private StringProperty amount = new SimpleStringProperty("");
    private StringProperty amountUnit = new SimpleStringProperty("");
    private StringProperty usage = new SimpleStringProperty("");
    private StringProperty days = new SimpleStringProperty("");
    private ObjectProperty<DrugCategory> category = new SimpleObjectProperty<>(DrugCategory.Naifuku);
    private StringProperty comment = new SimpleStringProperty("");

    public int getIyakuhincode() {
        return iyakuhincode;
    }

    public void setIyakuhincode(int iyakuhincode) {
        this.iyakuhincode = iyakuhincode;
    }

    public String getDrugName() {
        return drugName.get();
    }

    public StringProperty drugNameProperty() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName.set(drugName);
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getAmountUnit() {
        return amountUnit.get();
    }

    public StringProperty amountUnitProperty() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit.set(amountUnit);
    }

    public String getUsage() {
        return usage.get();
    }

    public StringProperty usageProperty() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage.set(usage);
    }

    public String getDays() {
        return days.get();
    }

    public StringProperty daysProperty() {
        return days;
    }

    public void setDays(String days) {
        this.days.set(days);
    }

    public DrugCategory getCategory() {
        return category.get();
    }

    public ObjectProperty<DrugCategory> categoryProperty() {
        return category;
    }

    public void setCategory(DrugCategory category) {
        this.category.set(category);
    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public void convertToDrug(int visitId, BiConsumer<DrugDTO, List<String>> cb){
        List<String> err = new ArrayList<>();
        DrugDTO drug = new DrugDTO();
        if( getIyakuhincode() > 0 ){
            drug.iyakuhincode = getIyakuhincode();
        } else {
            err.add("医薬品が指定されていません。");
        }
        try {
            drug.amount = Double.parseDouble(getAmount());
        } catch(NumberFormatException ex){
            err.add("用量の入力が適切でありません。");
        }
        if( !getUsage().isEmpty() ){
            drug.usage = getUsage();
        } else {
            err.add("用法が入力されていません。");
        }
        if( getCategory() != null ){
            drug.category = getCategory().getCode();
        } else {
            err.add("Drug category is not specified.");
        }
        try {
            if( getCategory() == DrugCategory.Gaiyou ){
                drug.days = 1;
            } else {
                drug.days = Integer.parseInt(getDays());
            }
        } catch(NumberFormatException ex){
            err.add("日数・回数の入力が不適切です。");
        }
        if( visitId > 0 ){
            drug.visitId = visitId;
        } else {
            err.add("Invalid visitId.");
        }
        if( err.size() > 0 ){
            cb.accept(null, err);
        } else {
            cb.accept(drug, null);
        }
    }
}
