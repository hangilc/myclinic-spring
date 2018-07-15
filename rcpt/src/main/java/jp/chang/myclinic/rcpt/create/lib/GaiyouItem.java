package jp.chang.myclinic.rcpt.create.lib;

import jp.chang.myclinic.util.RcptUtil;

import java.util.Objects;

public class GaiyouItem<T> implements RcptItem, Mergeable<GaiyouItem<T>>  {

    private int iyakuhincode;
    private String usage;
    private double amount;
    private String tekiyou;
    private double yakka;
    private int count;
    private T data;

    public GaiyouItem(int iyakuhincode, String usage, double amount, double yakka, String tekiyou, T data) {
        this.iyakuhincode = iyakuhincode;
        this.usage = usage;
        this.amount = amount;
        this.tekiyou = tekiyou;
        this.yakka = yakka;
        this.count = 1;
        this.data = data;
    }

    @Override
    public boolean canMerge(GaiyouItem<T> src) {
        return iyakuhincode == src.iyakuhincode && Objects.equals(usage, src.usage) &&
                amount == src.amount && Objects.equals(tekiyou, src.tekiyou);
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

    public T getData() {
        return data;
    }

    public String getTekiyou() {
        return tekiyou;
    }
}
