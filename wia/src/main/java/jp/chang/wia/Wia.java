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

	public static class Device {
		public String deviceId;
		public String name;
		public String description;

		@Override
		public String toString(){
			return "Wia.Device[" +
				"deviceId:" + deviceId + ", " +
				"name:" + name + ", " +
				"description:" + description +
			"]";
		}
	}

	// static {
	// 	HRESULT hr = Ole32.INSTANCE.CoInitialize(null);
	// 	if( !(hr.equals(WinError.S_OK) || hr.equals(WinError.S_FALSE)) ){
	// 		throw new RuntimeException("CoInitialize failed");
	// 	}
	// }

    public static WiaDevMgr createWiaDevMgr(){
        PointerByReference pp = new PointerByReference();
        HRESULT hr = Ole32.INSTANCE.CoCreateInstance(WiaConsts.CLSID_WiaDevMgr, null, 
            CLSCTX_LOCAL_SERVER, IWiaDevMgr.IID_IWiaDevMgr, pp);
        COMUtils.checkRC(hr);
        WiaDevMgr wiaDevMgr = new WiaDevMgr(pp.getValue());
        return wiaDevMgr;        
    }
	
	public static List<Device> listDevices(){
        PointerByReference pp = new PointerByReference();
        HRESULT hr = Ole32.INSTANCE.CoCreateInstance(WiaConsts.CLSID_WiaDevMgr, null, 
            CLSCTX_LOCAL_SERVER, IWiaDevMgr.IID_IWiaDevMgr, pp);
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
        		WiaPropertyStorage.ByReference storage = storages[0];
                PropValue[] values = readProps(storage, 
                    new int[]{ WiaConsts.WIA_DIP_DEV_ID, WiaConsts.WIA_DIP_DEV_NAME, WiaConsts.WIA_DIP_DEV_DESC });
                Device device = new Device();
                device.deviceId = values[0].getString();
                device.name = values[1].getString();
                device.description = values[2].getString();
                devices.add(device);
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

    public static PropValue[] readProps(WiaPropertyStorage storage, int[] propIds){
        int n = propIds.length;
        PROPSPEC spc = new PROPSPEC();
        PROPSPEC[] specs = (PROPSPEC[])spc.toArray(n);
        for(int i=0;i<n;i++){
            PROPSPEC spec = specs[i];
            spec.kind = new ULONG(PROPSPEC.PRSPEC_PROPID);
            spec.value.setType(ULONG.class);
            spec.value.propid = new ULONG(propIds[i]);
        }
        PROPVARIANT val = new PROPVARIANT();
        PROPVARIANT[] values = (PROPVARIANT[])val.toArray(n);
        HRESULT hr = storage.ReadMultiple(n, specs, values);
        COMUtils.checkRC(hr);
        PropValue[] retVals = new PropValue[n];
        for(int i=0;i<n;i++){
            retVals[i] = values[i].toPropValue();
        }
        hr = MyOle32.INSTANCE.FreePropVariantArray(new ULONG(n), values);
        COMUtils.checkRC(hr);
        return retVals;
    }
}