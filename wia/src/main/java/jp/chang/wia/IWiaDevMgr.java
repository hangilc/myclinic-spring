package jp.chang.wia;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Callback;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.Guid.REFIID;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.COM.IUnknown;

import java.util.List;
import java.util.Arrays;

public interface IWiaDevMgr extends IUnknown {
	public static final IID IID_IWiaDevMgr = new IID("5eb2502a-8cf1-11d1-bf92-0060081ed811");

	HRESULT EnumDeviceInfo(long flag, PointerByReference pp);
	HRESULT CreateDevice(BSTR deviceID, PointerByReference pp);
	HRESULT GetImageDlg(HWND hwndParent, LONG lDeviceType, LONG lFlagas, LONG lIntent,
		IWiaItem pItemRoot, BSTR bstrFileName, GUID pguidFormat);
	HRESULT SelectDeviceDlgID(HWND hwndParent, LONG lDeviceType, LONG lFlags, PointerByReference pbstrDeviceID);
}

