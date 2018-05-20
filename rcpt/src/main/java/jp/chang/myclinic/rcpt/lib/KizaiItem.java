package jp.chang.myclinic.rcpt.lib;

import jp.chang.myclinic.rcpt.RcptUtil;

public class KizaiItem<T> implements RcptItem, Mergeable<KizaiItem<T>> {

    private int kizaicode;
    private double amount;
    private double kingaku;
    private int count;
    private T kizai;

    public KizaiItem(int kizaicode, double amount, double kingaku, T kizai) {
        this.kizaicode = kizaicode;
        this.amount = amount;
        this.kingaku = kingaku;
        this.count = 1;
        this.kizai = kizai;
    }

    @Override
    public boolean canMerge(KizaiItem<T> src) {
        return kizaicode == src.kizaicode && amount == src.amount;
    }

    @Override
    public void merge(KizaiItem<T> src) {
        count += src.count;
    }

    @Override
    public int getTanka() {
        return RcptUtil.kizaiKingakuToTen(amount * kingaku);
    }

    @Override
    public int getCount() {
        return count;
    }

    public T getKizai() {
        return kizai;
    }

}
