package jp.chang.wia;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Callback;
import com.sun.jna.ptr.IntByReference;
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

public interface IWiaDataTransfer extends IUnknown {

	static final IID IID_IWiaDataTransfer = 
		new IID("a6cef998-a5b0-11d2-a08f-00c04f72dc3c");

	HRESULT idtGetData(STGMEDIUM medium, WiaDataCallback callback);

}