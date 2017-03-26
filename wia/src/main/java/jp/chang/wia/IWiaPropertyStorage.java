package jp.chang.wia;

import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.Memory;
import com.sun.jna.WString;

public interface IWiaPropertyStorage extends IUnknown, WiaTypes {

	public static final IID IID_IWiaPropertyStorage = new IID("98B5E8A0-29CC-491a-AAC0-E6DB4FDCCEB6");

	HRESULT ReadMultiple(int n, PROPSPEC[] propspecs, PROPVARIANT[] propvars);
	HRESULT WriteMultiple(ULONG n, PROPSPEC[] specs, PROPVARIANT[] values, PROPID propidNameFirst);
}