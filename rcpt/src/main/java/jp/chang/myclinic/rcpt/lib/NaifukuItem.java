package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.rcpt.RcptUtil;

import java.util.*;

public class NaifukuItem<T> implements RcptItem, Mergeable<NaifukuItem<T>>, Extendable<NaifukuItem<T>> {

    private static class NaifukuItemDrug {
        int iyakuhincode;
        double amount;

        public NaifukuItemDrug(int iyakuhincode, double amount) {
            this.iyakuhincode = iyakuhincode;
            this.amount = amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NaifukuItemDrug that = (NaifukuItemDrug) o;
            return iyakuhincode == that.iyakuhincode &&
                    Double.compare(that.amount, amount) == 0;
        }

        @Override
        public int hashCode() {

            return Objects.hash(iyakuhincode, amount);
        }
    }

    private String usage;
    private int days;
    private Set<NaifukuItemDrug> itemDrugs = new LinkedHashSet<>();
    private double kingaku;
    private List<T> drugs = new ArrayList<>();

    public NaifukuItem(String usage, int days, int iyakuhincode, double amount, double yakka, T drug) {
        this.usage = usage;
        this.days = days;
        itemDrugs.add(new NaifukuItemDrug(iyakuhincode, amount));
        this.kingaku = yakka * amount;
        drugs.add(drug);
    }

    @Override
    public boolean canExtend(NaifukuItem<T> arg){
        return Objects.equals(usage, arg.usage) && days == arg.days;
    }

    @Override
    public void extend(NaifukuItem<T> arg){
        this.itemDrugs.addAll(arg.itemDrugs);
        kingaku += arg.kingaku;
        this.drugs.addAll(arg.drugs);
    }

    @Override
    public boolean canMerge(NaifukuItem src){
        return Objects.equals(usage, src.usage) && 
                itemDrugs.equals(src.itemDrugs);
    }

    @Override
    public void merge(NaifukuItem src){
        days += src.days;
    }

    @Override
    public int getTanka(){
        return RcptUtil.touyakuKingakuToTen(kingaku);
    }

    @Override
    public int getCount(){
        return getDays();
    }

    public int getDays(){
        return days;
    }

    public String getUsage() {
        return usage;
    }

    public List<T> getDrugs() {
        return drugs;
    }
}
