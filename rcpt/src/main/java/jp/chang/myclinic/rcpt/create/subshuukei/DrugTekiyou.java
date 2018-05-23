package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

class DrugTekiyou implements Tekiyou {

    private static class Drug {
        String name;
        double amount;
        String unit;

        Drug(String name, double amount, String unit) {
            this.name = name;
            this.amount = amount;
            this.unit = unit;
        }
    }

    private int tanka;
    private int count;
    private List<Drug> drugs = new ArrayList<>();

    DrugTekiyou(int tanka, int count) {
        this.tanka = tanka;
        this.count = count;
    }

    void addDrug(String name, double amount, String unit){
        drugs.add(new Drug(name, amount, unit));
    }

    @Override
    public void output(String shuukei) {
        System.out.printf("tekiyou_begin_drugs %s:%d:%d\n", shuukei, tanka, count);
        drugs.forEach(drug ->
            System.out.printf("tekiyou_drug %s:%s%s\n", drug.name,
                    NumberUtil.formatNumber(drug.amount), drug.unit));
        System.out.println("tekiyou_end_drugs");
    }
}
