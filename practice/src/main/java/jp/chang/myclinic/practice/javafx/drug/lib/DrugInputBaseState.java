package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.Zaikei;
import jp.chang.myclinic.dto.*;

import java.util.Objects;

public class DrugInputBaseState {

    private int iyakuhincode;
    private String drugName = "";
    private String amount = "";
    private String amountUnit = "";
    private String usage = "";
    private String days = "";
    private DrugCategory category = DrugCategory.Naifuku;

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
        switch(category){
            case Naifuku: case Gaiyou: return "用量：";
            case Tonpuku: return "一回：";
            default: return "????：";
        }
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
        switch(category){
            case Naifuku: return "日数：";
            case Tonpuku: return "回数：";
            default: return "????：";
        }
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDaysUnit() {
        switch(category){
            case Naifuku: return "日分";
            case Tonpuku: return "回分";
            default: return "????";
        }
    }

    public DrugCategory getCategory() {
        return category;
    }

    public void setCategory(DrugCategory category) {
        this.category = category;
    }

    public boolean isDaysVisible() {
        switch(category){
            case Naifuku: case Tonpuku: return true;
            default: return false;
        }
    }

    public void assignTo(DrugInputBaseState dst){
        dst.setIyakuhincode(getIyakuhincode());
        dst.setDrugName(getDrugName());
        dst.setAmount(getAmount());
        dst.setAmountUnit(getAmountUnit());
        dst.setUsage(getUsage());
        dst.setDays(getDays());
        dst.setCategory(getCategory());
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
                Objects.equals(drugName, that.drugName) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(amountUnit, that.amountUnit) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(days, that.days) &&
                category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iyakuhincode, drugName, amount, amountUnit, usage,
                days, category);
    }

    @Override
    public String toString() {
        return "DrugInputBaseState{" +
                "iyakuhincode=" + iyakuhincode +
                ", drugName='" + drugName + '\'' +
                ", amount='" + amount + '\'' +
                ", amountUnit='" + amountUnit + '\'' +
                ", usage='" + usage + '\'' +
                ", days='" + days + '\'' +
                ", category=" + category +
                '}';
    }
}
