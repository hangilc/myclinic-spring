package jp.chang.wia;

import com.sun.jna.Pointer;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WinDef.LONG;

public class EnumWiaItem extends Unknown implements IEnumWiaItem {

	public EnumWiaItem(){}

	public EnumWiaItem(Pointer pointer){
		super(pointer);
	}

	@Override
	public HRESULT Next(int n, WiaItem.ByReference[] items, IntByReference nFetched){
		HRESULT hr = (HRESULT)_invokeNativeObject(3, new Object[]{
			this.getPointer(), n, items, nFetched
		}, HRESULT.class);
		return hr;
	}

}
