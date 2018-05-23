package jp.chang.myclinic.rcpt.lib;

public class ShinryouItem<T> extends RcptItemBase implements Mergeable<ShinryouItem<T>>, Eqv {

    private int shinryoucode;
    private T data;

    public ShinryouItem(int shinryoucode, int tensuu, T data) {
        super(tensuu, 1);
        this.shinryoucode = shinryoucode;
        this.data = data;
    }

    public int getShinryoucode() {
        return shinryoucode;
    }

    public T getData(){
        return data;
    }

    @Override
    public boolean canMerge(ShinryouItem<T> src) {
        return shinryoucode == src.shinryoucode;
    }

    @Override
    public void merge(ShinryouItem<T> src) {
        incCount(src.getCount());
    }

    @Override
    public String eqvCode() {
        return String.format("%d:%d", shinryoucode, getCount());
    }
}
