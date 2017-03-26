package jp.chang.wia;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Callback;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.Guid.REFIID;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.COM.IUnknown;

import java.util.List;
import java.util.Arrays;

public interface IWiaDevMgr2 extends IUnknown {
	public static final IID IID_IWiaDevMgr2 = new IID("79C07CF1-CBDD-41ee-8EC3-F00080CADA7A");

	EnumWIA_DEV_INFO EnumDeviceInfo(int flag);
	WiaItem2 CreateDevice(BSTR deviceID);
}

