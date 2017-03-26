package jp.chang.wia;

import com.sun.jna.Pointer;
import com.sun.jna.NativeLong;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WTypes.BSTR;

public class WiaDevMgr extends Unknown implements IWiaDevMgr {

	public WiaDevMgr(){

	}

	public WiaDevMgr(Pointer pointer){
		super(pointer);
	}

	@Override
	public HRESULT EnumDeviceInfo(long flag, PointerByReference pp){
		return (HRESULT) this._invokeNativeObject(3,
			new Object[]{ this.getPointer(), new NativeLong(flag), pp }, HRESULT.class);
	}
	// public EnumWIA_DEV_INFO EnumDeviceInfo(long flag){
	// 	PointerByReference pp = new PointerByReference();
	// 	HRESULT hr = (HRESULT) this._invokeNativeObject(3,
	// 		new Object[]{ this.getPointer(), new NativeLong(flag), pp }, HRESULT.class);
	// 	COMUtils.checkRC(hr);
	// 	return new EnumWIA_DEV_INFO(pp.getValue());
	// }

	@Override
	public HRESULT CreateDevice(BSTR deviceID, PointerByReference pp){
		return (HRESULT) this._invokeNativeObject(4,
			new Object[]{ this.getPointer(), deviceID, pp }, HRESULT.class);
	}
	// public WiaItem CreateDevice(BSTR deviceID){
	// 	PointerByReference pp = new PointerByReference();
	// 	HRESULT hr = (HRESULT) this._invokeNativeObject(4,
	// 		new Object[]{ this.getPointer(), deviceID, pp }, HRESULT.class);
	// 	if( hr.intValue() != 0 ){
	// 		System.err.printf("HRESULT: %x\n", hr.intValue());
	// 	}
	// 	COMUtils.checkRC(hr);
	// 	return new WiaItem(pp.getValue());
	// }
}