package jp.chang.myclinic.winutil;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WTypes;

public class OleString {

    //private static Logger logger = LoggerFactory.getLogger(OleString.class);
    Pointer pointer;
    private WTypes.LPOLESTR olestr;

    public OleString(String src) {
        Pointer pointer = Ole32.INSTANCE.CoTaskMemAlloc((src.length()+1)*2);
        olestr = new WTypes.LPOLESTR(pointer);
        olestr.setValue(src);
    }

    public void close(){
        Ole32.INSTANCE.CoTaskMemFree(pointer);
    }

    public WTypes.LPOLESTR asOleStr(){
        return olestr;
    }

}
