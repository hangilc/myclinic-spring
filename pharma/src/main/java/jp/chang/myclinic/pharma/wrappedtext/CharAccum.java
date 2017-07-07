package jp.chang.myclinic.pharma.wrappedtext;

public class CharAccum {

    private int remain;
    private StringBuilder chars = new StringBuilder();

    public CharAccum(int width){
        this.remain = width;
    }

    public int getRemaining(){
        return remain;
    }

    public void addChar(char ch, int charWidth){
        chars.append(ch);
        remain -= charWidth;
    }

    public String getString(){
        return chars.toString();
    }

    public int numberOfChars(){
        return chars.length();
    }

}
