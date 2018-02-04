package jp.chang.myclinic.practice.javafx.drug;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.DrugCategory;

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
}
