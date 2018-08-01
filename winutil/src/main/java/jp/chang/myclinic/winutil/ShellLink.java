package jp.chang.myclinic.winutil;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.WinNT.HRESULT;

public class ShellLink extends Unknown implements IShellLink{

    //private static Logger logger = LoggerFactory.getLogger(ShellLink.class);

    public ShellLink() {

    }

    public ShellLink(Pointer pointer){
        super(pointer);
    }

    @Override
    public HRESULT GetPath(Pointer pszFile, int cchMaxPath, Pointer pdf, int fFlags) {
        return (HRESULT) this._invokeNativeObject(3,
                new Object[]{ this.getPointer(), pszFile, cchMaxPath, pdf, fFlags }, HRESULT.class);

    }

    @Override
    public HRESULT SetPath(Pointer pszFile) {
        return (HRESULT) this._invokeNativeObject(20,
                new Object[]{ this.getPointer(), pszFile }, HRESULT.class);
    }

}
