package jp.chang.myclinic.drawer.printer;

import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinDef;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hangil on 2017/05/24.
 */
public class DOCINFO extends Structure implements WinDef {

    public int cbSize;
    public WString docName;
    public WString output;
    public WString dataType;
    public DWORD fwType;

    @Override
    protected List<String> getFieldOrder(){
        return Arrays.asList(
                "cbSize", "docName", "output", "dataType", "fwType"
        );
    }
}
