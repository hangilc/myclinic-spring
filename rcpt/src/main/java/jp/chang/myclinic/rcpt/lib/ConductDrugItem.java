package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.rcpt.RcptUtil;

public class ConductDrugItem<T> implements RcptItem, Mergeable<ConductDrugItem<T>>{

    private ConductKind kind;
    private int iyakuhincode;
    private double amount;
    private double yakka;
    private int count;
    private T drug;

    public ConductDrugItem(ConductKind kind, int iyakuhincode, double amount, double yakka, T drug) {
        this.kind = kind;
        this.iyakuhincode = iyakuhincode;
        this.amount = amount;
        this.yakka = yakka;
        this.count = 1;
        this.drug = drug;
    }

    @Override
    public boolean canMerge(ConductDrugItem src) {
        return iyakuhincode == src.iyakuhincode && amount == src.amount;
    }

    @Override
    public void merge(ConductDrugItem src) {
        count += src.count;
    }

    @Override
    public int getTanka() {
        if( kind == ConductKind.Gazou ){
            return RcptUtil.shochiKingakuToTen(amount * yakka);
        } else {
            return RcptUtil.touyakuKingakuToTen(amount * yakka);
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    public T getDrug() {
        return drug;
    }

}
