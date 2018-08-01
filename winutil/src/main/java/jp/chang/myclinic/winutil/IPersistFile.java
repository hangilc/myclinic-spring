package jp.chang.myclinic.winutil;

import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.WTypes.*;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinNT.HRESULT;

public interface IPersistFile extends IUnknown {

    IID IID_IPersistFile = new IID("0000010b-0000-0000-C000-000000000046");

    HRESULT Save(LPOLESTR pszFileName, BOOL fRememebr);
}
