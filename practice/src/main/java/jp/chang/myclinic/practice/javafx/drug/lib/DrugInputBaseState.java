package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;

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

    public DrugInputBaseState() {

    }

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
        adaptToCategory();
    }

    public boolean isDaysVisible(){
        return category == DrugCategory.Naifuku || category == DrugCategory.Tonpuku;
    }

    private void adaptToCategory(){
        if (category != null) {
            switch (category) {
                case Naifuku: {
                    setAmountLabel("用量：");
                    setDaysLabel("日数：");
                    setDaysUnit("日分");
                    break;
                }
                case Tonpuku: {
                    setAmountLabel("一回：");
                    setDaysLabel("回数：");
                    setDaysUnit("回分");
                    break;
                }
                case Gaiyou: {
                    setAmountLabel("用量：");
                    break;
                }
            }
        }
    }

}
