package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.rcpt.RcptUtil;

import java.util.Objects;

public class TonpukuItem<T> implements RcptItem, Mergeable<TonpukuItem<T>> {

    private int iyakuhincode;
    private String usage;
    private double amount;
    private double yakka;
    private int count;
    private T drug;

    public TonpukuItem(int iyakuhincode, String usage, double amount, double yakka, int count, T drug) {
        this.iyakuhincode = iyakuhincode;
        this.usage = usage;
        this.amount = amount;
        this.yakka = yakka;
        this.count = count;
        this.drug = drug;
    }

    @Override
    public int getTanka() {
        return RcptUtil.touyakuKingakuToTen(amount * yakka);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean canMerge(TonpukuItem<T> src) {
        return iyakuhincode == src.iyakuhincode && Objects.equals(usage, src.usage) &&
                amount == src.amount;
    }

    @Override
    public void merge(TonpukuItem<T> src) {
        count += src.count;
    }

    public int getIyakuhincode() {
        return iyakuhincode;
    }

    public T getDrug() {
        return drug;
    }

}
