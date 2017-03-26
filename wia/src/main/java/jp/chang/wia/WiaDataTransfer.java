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

public class WiaDataTransfer extends Unknown implements IWiaDataTransfer {

	public static class ByReference extends WiaDataTransfer implements Structure.ByReference {} 

	public WiaDataTransfer(){}

	public WiaDataTransfer(Pointer pointer){
		super(pointer);
	}

	@Override
	public HRESULT idtGetData(STGMEDIUM medium, WiaDataCallback callback){
		return (HRESULT)_invokeNativeObject(3, new Object[]{
			this.getPointer(), medium, callback
		}, HRESULT.class);
	}

}
