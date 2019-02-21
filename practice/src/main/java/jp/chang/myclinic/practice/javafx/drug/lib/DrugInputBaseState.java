package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.Zaikei;
import jp.chang.myclinic.dto.*;

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

    public DrugInputBaseState() {
        adaptToCategory();
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
    }

    public boolean isDaysVisible() {
        return daysVisible;
    }

    public void setDaysVisible(boolean daysVisible) {
        this.daysVisible = daysVisible;
    }

    public void assignTo(DrugInputBaseState dst){
        dst.setIyakuhincode(getIyakuhincode());
        dst.setDrugName(getDrugName());
        dst.setAmountLabel(getAmountLabel());
        dst.setAmount(getAmount());
        dst.setAmountUnit(getAmountUnit());
        dst.setUsage(getUsage());
        dst.setDaysLabel(getDaysLabel());
        dst.setDays(getDays());
        dst.setDaysUnit(getDaysUnit());
        dst.setCategory(getCategory());
        dst.setDaysVisible(isDaysVisible());
    }

    void clear(){
        DrugInputBaseState src = new DrugInputBaseState();
        src.setCategory(category);
        src.adaptToCategory();
        src.assignTo(this);
    }

    public DrugInputBaseState copy(){
        DrugInputBaseState state = new DrugInputBaseState();
        assignTo(state);
        return state;
    }

    void adaptToCategory(){
        if (category != null) {
            switch (category) {
                case Naifuku: {
                    setAmountLabel("用量：");
                    setDaysLabel("日数：");
                    setDaysUnit("日分");
                    setDaysVisible(true);
                    break;
                }
                case Tonpuku: {
                    setAmountLabel("一回：");
                    setDaysLabel("回数：");
                    setDaysUnit("回分");
                    setDaysVisible(true);
                    break;
                }
                case Gaiyou: {
                    setAmountLabel("用量：");
                    setDaysVisible(false);
                    break;
                }
            }
        }
    }

    void setMaster(IyakuhinMasterDTO master){
        DrugCategory category = DrugCategory.Naifuku;
        if( Zaikei.fromCode(master.zaikei) == Zaikei.Gaiyou ){
            category = DrugCategory.Gaiyou;
        }
        setIyakuhincode(master.iyakuhincode);
        setDrugName(master.name);
        setAmount("");
        setAmountUnit(master.unit);
        setUsage("");
        setDays("");
        setCategory(category);
        adaptToCategory();
    }

    void setPrescExample(PrescExampleFullDTO exampleFull){
        PrescExampleDTO example = exampleFull.prescExample;
        DrugCategory exampleCategory = DrugCategory.fromCode(example.category);
        setMaster(exampleFull.master);
        setAmount(example.amount);
        setUsage(example.usage);
        setDays(example.days + "");
        setCategory(exampleCategory);
        adaptToCategory();
    }

    void setDrug(DrugFullDTO drugFull){
        DrugHelper helper = new DrugHelper();
        DrugDTO drug = drugFull.drug;
        DrugCategory drugCategory = DrugCategory.fromCode(drug.category);
        setMaster(drugFull.master);
        setAmount(helper.formatAmount(drug.amount));
        setUsage(drug.usage);
        setDays(drug.days + "");
        setCategory(drugCategory);
        adaptToCategory();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrugInputBaseState)) return false;
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
