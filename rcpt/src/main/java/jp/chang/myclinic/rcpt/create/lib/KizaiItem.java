package jp.chang.myclinic.rcpt.create.lib;

import jp.chang.myclinic.util.RcptUtil;
import jp.chang.myclinic.util.NumberUtil;

public class KizaiItem<T> implements RcptItem, Mergeable<KizaiItem<T>>, Eqv {

    private int kizaicode;
    private double amount;
    private double kingaku;
    private int count;
    private T data;

    public KizaiItem(int kizaicode, double amount, double kingaku, T data) {
        this.kizaicode = kizaicode;
        this.amount = amount;
        this.kingaku = kingaku;
        this.count = 1;
        this.data = data;
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

    public T getData() {
        return data;
    }

    @Override
    public String eqvCode() {
        return String.format("%d:%s:%d", kizaicode, NumberUtil.formatNumber(amount), count);
    }

}
