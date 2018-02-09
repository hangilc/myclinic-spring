package jp.chang.myclinic.practice.lib;

public class DrugInputConstraints {

    private boolean amountFixed = false;
    private boolean usageFixed = false;
    private boolean daysFixed = false;

    public boolean isAmountFixed() {
        return amountFixed;
    }

    public void setAmountFixed(boolean amountFixed) {
        this.amountFixed = amountFixed;
    }

    public boolean isUsageFixed() {
        return usageFixed;
    }

    public void setUsageFixed(boolean usageFixed) {
        this.usageFixed = usageFixed;
    }

    public boolean isDaysFixed() {
        return daysFixed;
    }

    public void setDaysFixed(boolean daysFixed) {
        this.daysFixed = daysFixed;
    }

}
