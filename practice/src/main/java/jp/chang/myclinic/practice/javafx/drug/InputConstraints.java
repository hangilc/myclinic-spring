package jp.chang.myclinic.practice.javafx.drug;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class InputConstraints {

    private BooleanProperty amountFixed = new SimpleBooleanProperty(false);
    private BooleanProperty usageFixed = new SimpleBooleanProperty(false);
    private BooleanProperty daysFixed = new SimpleBooleanProperty(false);

    public boolean isAmountFixed() {
        return amountFixed.get();
    }

    public BooleanProperty amountFixedProperty() {
        return amountFixed;
    }

    public void setAmountFixed(boolean amountFixed) {
        this.amountFixed.set(amountFixed);
    }

    public boolean isUsageFixed() {
        return usageFixed.get();
    }

    public BooleanProperty usageFixedProperty() {
        return usageFixed;
    }

    public void setUsageFixed(boolean usageFixed) {
        this.usageFixed.set(usageFixed);
    }

    public boolean isDaysFixed() {
        return daysFixed.get();
    }

    public BooleanProperty daysFixedProperty() {
        return daysFixed;
    }

    public void setDaysFixed(boolean daysFixed) {
        this.daysFixed.set(daysFixed);
    }

}
