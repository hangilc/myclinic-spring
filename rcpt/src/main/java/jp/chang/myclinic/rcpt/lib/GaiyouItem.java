package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.rcpt.RcptUtil;

import java.util.Objects;

public class GaiyouItem<T> implements RcptItem, Mergeable<GaiyouItem<T>>  {

    private int iyakuhincode;
    private String usage;
    private double amount;
    private double yakka;
    private int count;
    private T drug;

    public GaiyouItem(int iyakuhincode, String usage, double amount, double yakka, T drug) {
        this.iyakuhincode = iyakuhincode;
        this.usage = usage;
        this.amount = amount;
        this.yakka = yakka;
        this.count = 1;
        this.drug = drug;
    }

    @Override
    public boolean canMerge(GaiyouItem<T> src) {
        return iyakuhincode == src.iyakuhincode && Objects.equals(usage, src.usage) &&
                amount == src.amount;
    }

    @Override
    public void merge(GaiyouItem<T> src) {
        count += src.count;
    }

    @Override
    public int getTanka() {
        return RcptUtil.touyakuKingakuToTen(amount * yakka);
    }

    @Override
    public int getCount() {
        return count;
    }

    public T getDrug() {
        return drug;
    }
}
