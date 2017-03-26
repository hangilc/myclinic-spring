package jp.chang.wia;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Callback;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.Guid.REFIID;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.COM.IUnknown;

import java.util.List;
import java.util.Arrays;

public interface IEnumWiaItem2 extends IUnknown {

	HRESULT Next(ULONG n, WiaItem2.ByReference[] items, IntByReference nFetched);
}