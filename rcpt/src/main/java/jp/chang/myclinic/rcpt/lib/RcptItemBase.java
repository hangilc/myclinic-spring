package jp.chang.myclinic.rcpt.lib;

public class RcptItemBase implements RcptItem {

    private int tanka;
    private int count;

    public RcptItemBase(int tanka, int count) {
        this.tanka = tanka;
        this.count = count;
    }

    @Override
    public int getTanka() {
        return tanka;
    }

    @Override
    public int getCount() {
        return count;
    }

    void incTanka(int n){
        tanka += n;
    }

    void incCount(int n){
        count += n;
    }

}
