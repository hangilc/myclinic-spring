package jp.chang.myclinic.winutil;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.WTypes.LPOLESTR;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.BOOL;
import com.sun.jna.platform.win32.WinNT.HRESULT;

public class PersistFile extends Unknown implements IPersistFile {

    //private static Logger logger = LoggerFactory.getLogger(PersistFile.class);

    public PersistFile() {

    }

    public PersistFile(Pointer pointer){
        super(pointer);
    }

    @Override
    public HRESULT Load(LPOLESTR pszFileName, WinDef.DWORD dwMode) {
        return (HRESULT)this._invokeNativeObject(5,
                new Object[]{ this.getPointer(), pszFileName, dwMode}, HRESULT.class);
    }

    @Override
    public HRESULT Save(LPOLESTR pszFileName, BOOL fRememebr) {
        return (HRESULT)this._invokeNativeObject(6,
                new Object[]{ this.getPointer(), pszFileName, fRememebr}, HRESULT.class);
    }

}
