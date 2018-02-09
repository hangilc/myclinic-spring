package jp.chang.myclinic.practice.lib.drug;

public interface DrugInputConstraints {

    boolean isAmountFixed();
    boolean isUsageFixed();
    boolean isDaysFixed();

    static DrugInputConstraints defaultDrugInputConstraints(){
        return new DrugInputConstraints() {
            @Override
            public boolean isAmountFixed() {
                return false;
            }

            @Override
            public boolean isUsageFixed() {
                return false;
            }

            @Override
            public boolean isDaysFixed() {
                return false;
            }
        };
    }

}
