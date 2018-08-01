package jp.chang.myclinic.winutil;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.WinNT.HRESULT;

public interface IShellLink extends IUnknown {

    IID IID_IShellLink = new IID("000214F9-0000-0000-C000-000000000046");

    HRESULT GetPath(Pointer pszFile, int cchMaxPath, Pointer pdf, int fFlags);
    HRESULT SetPath(Pointer pszFile);
}
