package jp.chang.wia;

import com.sun.jna.Pointer;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinDef.HWND;

public class WiaItem extends Unknown implements IWiaItem {

	public static class ByReference extends WiaItem implements Structure.ByReference {} 

	public WiaItem(){}

	public WiaItem(Pointer pointer){
		super(pointer);
	}

	@Override
	public int GetItemType(){
		IntByReference intPtr = new IntByReference();
		HRESULT hr = (HRESULT)_invokeNativeObject(3, new Object[]{
			this.getPointer(), intPtr
		}, HRESULT.class);
		COMUtils.checkRC(hr);
		return intPtr.getValue();
	}

	@Override
	public EnumWiaItem EnumChildItems(){
		PointerByReference ptr = new PointerByReference();
		HRESULT hr = (HRESULT)_invokeNativeObject(5, new Object[]{
			this.getPointer(), ptr
		}, HRESULT.class);
		COMUtils.checkRC(hr);
		return new EnumWiaItem(ptr.getValue());
	}

	@Override
	public HRESULT DeviceDlg(HWND parent, LONG flags, LONG intent, 
		IntByReference itemCount, PointerByReference items){
		return (HRESULT)_invokeNativeObject(10, new Object[]{
			this.getPointer(), parent, flags, intent, itemCount, items
		}, HRESULT.class);
	}


}
