package jp.chang.myclinic.rcpt.lib;

public class ShinryouItem {

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

    public int getShinryoucode() {
        return shinryoucode;
    }

    public String getName() {
        return name;
    }

    public int getTensuu() {
        return tensuu;
    }

    public int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }
}
