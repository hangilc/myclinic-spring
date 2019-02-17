package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;

import java.util.Objects;

public class DrugInputBaseState {

    private int iyakuhincode;
    private String drugName = "";
    private String amountLabel = "";
    private String amount = "";
    private String amountUnit = "";
    private String usage = "";
    private String daysLabel = "";
    private String days = "";
    private String daysUnit = "";
    private DrugCategory category = DrugCategory.Naifuku;
    private boolean daysVisible = true;

    public int getIyakuhincode() {
        return iyakuhincode;
    }

    public void setIyakuhincode(int iyakuhincode) {
        this.iyakuhincode = iyakuhincode;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getAmountLabel() {
        return amountLabel;
    }

    public void setAmountLabel(String amountLabel) {
        this.amountLabel = amountLabel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDaysLabel() {
        return daysLabel;
    }

    public void setDaysLabel(String daysLabel) {
        this.daysLabel = daysLabel;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDaysUnit() {
        return daysUnit;
    }

    public void setDaysUnit(String daysUnit) {
        this.daysUnit = daysUnit;
    }

    public DrugCategory getCategory() {
        return category;
    }

    public void setCategory(DrugCategory category) {
        this.category = category;
    }

    public boolean isDaysVisible() {
        return daysVisible;
    }

    public void setDaysVisible(boolean daysVisible) {
        this.daysVisible = daysVisible;
    }

    public DrugInputBaseState copy(){
        DrugInputBaseState state = new DrugInputBaseState();
        state.setIyakuhincode(getIyakuhincode());
        state.setDrugName(getDrugName());
        state.setAmountLabel(getAmountLabel());
        state.setAmount(getAmount());
        state.setAmountUnit(getAmountUnit());
        state.setUsage(getUsage());
        state.setDaysLabel(getDaysLabel());
        state.setDays(getDays());
        state.setDaysUnit(getDaysUnit());
        state.setCategory(getCategory());
        state.setDaysVisible(isDaysVisible());
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrugInputBaseState that = (DrugInputBaseState) o;
        return iyakuhincode == that.iyakuhincode &&
                daysVisible == that.daysVisible &&
                Objects.equals(drugName, that.drugName) &&
                Objects.equals(amountLabel, that.amountLabel) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(amountUnit, that.amountUnit) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(daysLabel, that.daysLabel) &&
                Objects.equals(days, that.days) &&
                Objects.equals(daysUnit, that.daysUnit) &&
                category == that.category;
    }

    @Override
    public int hashCode() {

        return Objects.hash(iyakuhincode, drugName, amountLabel, amount, amountUnit, usage, daysLabel, days, daysUnit, category, daysVisible);
    }

    @Override
    public String toString() {
        return "DrugInputBaseState{" +
                "iyakuhincode=" + iyakuhincode +
                ", drugName='" + drugName + '\'' +
                ", amountLabel='" + amountLabel + '\'' +
                ", amount='" + amount + '\'' +
                ", amountUnit='" + amountUnit + '\'' +
                ", usage='" + usage + '\'' +
                ", daysLabel='" + daysLabel + '\'' +
                ", days='" + days + '\'' +
                ", daysUnit='" + daysUnit + '\'' +
                ", category=" + category +
                ", daysVisible=" + daysVisible +
                '}';
    }
}
