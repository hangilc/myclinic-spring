package jp.chang.myclinic.rcpt.create.lib;

import jp.chang.myclinic.util.RcptUtil;

import java.util.Objects;

public class TonpukuItem<T> implements RcptItem, Mergeable<TonpukuItem<T>> {

    private int iyakuhincode;
    private String usage;
    private double amount;
    private String tekiyou;
    private double yakka;
    private int count;
    private T drug;

    public TonpukuItem(int iyakuhincode, String usage, double amount, double yakka, int count,
                       String tekiyou, T drug) {
        this.iyakuhincode = iyakuhincode;
        this.usage = usage;
        this.amount = amount;
        this.tekiyou = tekiyou;
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
                amount == src.amount && Objects.equals(tekiyou, src.tekiyou);
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

    public String getTekiyou() {
        return tekiyou;
    }
}
