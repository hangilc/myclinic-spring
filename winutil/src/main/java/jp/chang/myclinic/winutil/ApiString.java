package jp.chang.myclinic.winutil;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class ApiString {

    //private static Logger logger = LoggerFactory.getLogger(ApiString.class);
    private Pointer pointer;

    public ApiString(int charSize) {
        this.pointer = new Memory(charSize * 2);
    }

    public ApiString(String src){
        this(src.length()+1);
        pointer.setWideString(0, src);
    }

    public Pointer asPointer(){
        return pointer;
    }

    @Override
    public String toString(){
        return pointer.getWideString(0);
    }

}
