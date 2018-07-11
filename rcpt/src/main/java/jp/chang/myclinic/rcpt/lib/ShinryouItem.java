package jp.chang.myclinic.rcpt.lib;

import java.util.Objects;

public class ShinryouItem<T> extends RcptItemBase implements Mergeable<ShinryouItem<T>>, Eqv {

    private int shinryoucode;
    private String tekiyou;
    private T data;

    public ShinryouItem(int shinryoucode, int tensuu, String tekiyou, T data) {
        super(tensuu, 1);
        this.shinryoucode = shinryoucode;
        this.tekiyou = tekiyou;
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
        return shinryoucode == src.shinryoucode && Objects.equals(tekiyou, src.tekiyou);
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
