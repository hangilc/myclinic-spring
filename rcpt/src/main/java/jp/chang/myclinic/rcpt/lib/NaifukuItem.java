package jp.chang.myclinic.rcpt.lib;

import java.util.*;

public class NaifukuItem<T> {

    private String usage;
    private int days;
    private Set<Integer> iyakuhincodes = new LinkedHashSet<>();
    private double kingaku;
    private List<T> drugs = new ArrayList<>();

    public NaifukuItem(String usage, int days, int iyakuhincode, double yakka, double amount, T drug) {
        this.usage = usage;
        this.days = days;
        iyakuhincodes.add(iyakuhincode);
        this.kingaku = yakka * amount;
        drugs.add(drug);
    }

    public boolean canExtend(String usage, int days){
        return Objects.equals(this.usage, usage) && this.days == days;
    }

    public void extend(int iyakuhincode, double yakka, double amount, T drug){
        iyakuhincodes.add(iyakuhincode);
        kingaku += yakka * amount;
        drugs.add(drug);
    }

    public boolean canMerge(NaifukuItem src){
        return Objects.equals(usage, src.usage) && days == src.days &&
                iyakuhincodes.equals(src.iyakuhincodes);
    }

    public void merge(NaifukuItem src){
        days += src.days;
    }

}
