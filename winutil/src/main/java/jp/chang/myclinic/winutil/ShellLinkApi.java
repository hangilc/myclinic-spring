package jp.chang.myclinic.winutil;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.WinNT.HRESULT;

public class ShellLinkApi extends Unknown implements IShellLink{

    //private static Logger logger = LoggerFactory.getLogger(ShellLink.class);

    public ShellLinkApi() {

    }

    public ShellLinkApi(Pointer pointer){
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

    @Override
    public HRESULT SetArguments(Pointer pszArgs) {
        return (HRESULT) this._invokeNativeObject(11,
                new Object[]{ this.getPointer(), pszArgs }, HRESULT.class);
    }

    @Override
    public HRESULT SetWorkingDirectory(Pointer pszDir) {
        return (HRESULT) this._invokeNativeObject(9,
                new Object[]{ this.getPointer(), pszDir }, HRESULT.class);
    }

}
