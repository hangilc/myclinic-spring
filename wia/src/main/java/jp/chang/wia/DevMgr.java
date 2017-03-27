package jp.chang.wia;

import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import static com.sun.jna.platform.win32.WTypes.CLSCTX_LOCAL_SERVER;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.List;
import java.util.ArrayList;
import java.io.Closeable;

public class DevMgr implements Closeable {

	private WiaDevMgr wiaDevMgr;

	public static DevMgr create(){
        PointerByReference pp = new PointerByReference();
        HRESULT hr = Ole32.INSTANCE.CoCreateInstance(WiaConsts.CLSID_WiaDevMgr2, null, 
            CLSCTX_LOCAL_SERVER, IWiaDevMgr2.IID_IWiaDevMgr2, pp);
        COMUtils.checkRC(hr);
        WiaDevMgr wiaDevMgr = new WiaDevMgr(pp.getValue());
        hr = wiaDevMgr.EnumDeviceInfo(WiaConsts.WIA_DEVINFO_ENUM_LOCAL, pp);
        COMUtils.checkRC(hr);
        return new DevMgr(wiaDevMgr);
	}

	private DevMgr(WiaDevMgr wiaDevMgr){
		this.wiaDevMgr = wiaDevMgr;
	}

	@Override
	public void close(){
		wiaDevMgr.Release();
	}

}