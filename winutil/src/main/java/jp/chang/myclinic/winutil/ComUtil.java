package jp.chang.myclinic.winutil;

import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT;

public class ComUtil {

    //private static Logger logger = LoggerFactory.getLogger(ComUtil.class);
    static {
        CoInitialize();
    }

    public static final Guid.CLSID CLSID_ShellLink  = new Guid.CLSID("00021401-0000-0000-C000-000000000046");

    private ComUtil() {

    }

    private static void CoInitialize(){
        WinNT.HRESULT hr = Ole32.INSTANCE.CoInitialize(null);
        if( !(hr.equals(WinError.S_OK) || hr.equals(WinError.S_FALSE)) ){
            throw new RuntimeException("CoInitialize failed");
        }
    }


}
