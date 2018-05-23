package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.rcpt.RcptUtil;

import java.util.*;

public class NaifukuItem<T> implements RcptItem, Mergeable<NaifukuItem<T>>, Extendable<NaifukuItem<T>> {

    private String usage;
    private int days;
    private Set<Integer> iyakuhincodes = new LinkedHashSet<>();
    private double kingaku;
    private List<T> drugs = new ArrayList<>();

    NaifukuItem(String usage, int days, int iyakuhincode, double yakkaTimesAmount, T drug) {
        this.usage = usage;
        this.days = days;
        iyakuhincodes.add(iyakuhincode);
        this.kingaku = yakkaTimesAmount;
        drugs.add(drug);
    }

    @Override
    public boolean canExtend(NaifukuItem<T> arg){
        return Objects.equals(usage, arg.usage) && days == arg.days;
    }

    @Override
    public void extend(NaifukuItem<T> arg){
        this.iyakuhincodes.addAll(arg.iyakuhincodes);
        kingaku += arg.kingaku;
        this.drugs.addAll(arg.drugs);
    }

    @Override
    public boolean canMerge(NaifukuItem src){
        return Objects.equals(usage, src.usage) && days == src.days &&
                iyakuhincodes.equals(src.iyakuhincodes);
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

    public Set<Integer> getIyakuhincodes() {
        return iyakuhincodes;
    }

    public double getKingaku() {
        return kingaku;
    }

    public List<T> getDrugs() {
        return drugs;
    }
}
