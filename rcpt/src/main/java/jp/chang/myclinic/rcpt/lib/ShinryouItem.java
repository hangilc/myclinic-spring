package jp.chang.myclinic.rcpt.lib;

public class ShinryouItem implements RcptItem, Mergeable<ShinryouItem> {

    private int shinryoucode;
    private String name;
    private int tensuu;
    private int count;

    public ShinryouItem(int shinryoucode, String name, int tensuu) {
        this.shinryoucode = shinryoucode;
        this.name = name;
        this.tensuu = tensuu;
        this.count = 1;
    }

    @Override
    public int getTanka() {
        return getTensuu();
    }

    public int getShinryoucode() {
        return shinryoucode;
    }

    public String getName() {
        return name;
    }

    public int getTensuu() {
        return tensuu;
    }

    @Override
    public int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean canMerge(ShinryouItem src) {
        return shinryoucode == src.shinryoucode;
    }

    @Override
    public void merge(ShinryouItem src) {
        count += src.count;
    }

    String eqvCode(ShinryouItem src){
        return String.format("%d:%d", shinryoucode, count);
    }
}
