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

public class Wia {

	public class Device {
		public String deviceId;
		public String name;
		public String description;
	}

	static {
		HRESULT hr = Ole32.INSTANCE.CoInitialize(null);
		if( !(hr.equals(WinError.S_OK) || hr.equals(WinError.S_FALSE)) ){
			throw new RuntimeException("CoInitialize failed");
		}
	}

    public static WiaDevMgr createWiaDevMgr(){
        PointerByReference pp = new PointerByReference();
        HRESULT hr = Ole32.INSTANCE.CoCreateInstance(WiaConsts.CLSID_WiaDevMgr2, null, 
            CLSCTX_LOCAL_SERVER, IWiaDevMgr2.IID_IWiaDevMgr2, pp);
        COMUtils.checkRC(hr);
        WiaDevMgr wiaDevMgr = new WiaDevMgr(pp.getValue());
        return wiaDevMgr;        
    }
	
	public static List<Device> listDevices(){
        PointerByReference pp = new PointerByReference();
        HRESULT hr = Ole32.INSTANCE.CoCreateInstance(WiaConsts.CLSID_WiaDevMgr2, null, 
            CLSCTX_LOCAL_SERVER, IWiaDevMgr2.IID_IWiaDevMgr2, pp);
        COMUtils.checkRC(hr);
        WiaDevMgr wiaDevMgr = new WiaDevMgr(pp.getValue());
        hr = wiaDevMgr.EnumDeviceInfo(WiaConsts.WIA_DEVINFO_ENUM_LOCAL, pp);
        COMUtils.checkRC(hr);
        EnumWIA_DEV_INFO wiaDevInfo = new EnumWIA_DEV_INFO(pp.getValue());
        List<Device> devices = new ArrayList<Device>();
        while(true){
	        WiaPropertyStorage.ByReference[] storages = new WiaPropertyStorage.ByReference[1];
        	IntByReference nFetched = new IntByReference();
        	hr = wiaDevInfo.Next(new ULONG(1), storages, nFetched);
        	COMUtils.checkRC(hr);
        	if( nFetched.getValue() > 0 ){
        		System.out.println("fetched");
        		WiaPropertyStorage.ByReference storage = storages[0];
        		storage.Release();
        	}
        	if( hr.equals(WinError.S_FALSE) ){
        		break;
        	}
        }
        wiaDevInfo.Release();
    	wiaDevMgr.Release();
        return devices;
	}
}