package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.consts.DrugCategory;

import java.util.Objects;

class DrugEnterInputState extends DrugInputState {

    private boolean isDaysFixed = true;
    private boolean daysFixedDisabled = false;
    private String daysBackup = "";

    DrugEnterInputState() {

    }

    DrugEnterInputState(DrugInputBaseState src) {
        src.assignTo(this);
    }

    private void assignTo(DrugEnterInputState dst){
        super.assignTo(dst);
        dst.isDaysFixed = isDaysFixed;
        dst.daysFixedDisabled = dst.isDaysFixedDisabled();
        dst.daysBackup = daysBackup;
    }

    void clear(){
        if( !isDaysFixed ){

        }
    }

    public DrugEnterInputState copy(){
        DrugEnterInputState dst = new DrugEnterInputState();
        assignTo(dst);
        return dst;
    }

    void adaptToDaysFixedChange(){
        if( getCategory() == DrugCategory.Naifuku ){
            if( isDaysFixed() ){
                setDays(getDaysBackup());
            } else {
                setDays("");
            }
        }
    }

    @Override
    void adaptToCategory(){
        super.adaptToCategory();
        DrugCategory category = getCategory();
        this.daysFixedDisabled = category != DrugCategory.Naifuku;
    }

    boolean isDaysFixed() {
        return isDaysFixed;
    }

    void setDaysFixed(boolean daysFixed) {
        isDaysFixed = daysFixed;
    }

    boolean isDaysFixedDisabled() {
        return daysFixedDisabled;
    }

    void setDaysFixedDisabled(boolean daysFixedDisabled) {
        this.daysFixedDisabled = daysFixedDisabled;
    }

    String getDaysBackup() {
        return daysBackup;
    }

    void setDaysBackup(String daysBackup) {
        this.daysBackup = daysBackup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrugEnterInputState)) return false;
        if (!super.equals(o)) return false;
        DrugEnterInputState that = (DrugEnterInputState) o;
        return isDaysFixed == that.isDaysFixed &&
                daysFixedDisabled == that.daysFixedDisabled &&
                Objects.equals(daysBackup, that.daysBackup);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), isDaysFixed, daysFixedDisabled, daysBackup);
    }

    @Override
    public String toString() {
        return "DrugEnterInputState{" +
                "isDaysFixed=" + isDaysFixed +
                ", daysFixedDisabled=" + daysFixedDisabled +
                ", daysBackup='" + daysBackup + '\'' +
                "} " + super.toString();
    }
}
