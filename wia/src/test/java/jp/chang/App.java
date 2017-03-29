package jp.chang;

import com.sun.jna.Native;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Guid.CLSID;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.Guid.REFIID;
import static com.sun.jna.platform.win32.Guid.IID_NULL;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WTypes.LPOLESTR;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinDef.ULONGByReference;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WTypes.VARTYPE;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Wincon;
import com.sun.jna.platform.win32.WinError;

import jp.chang.wia.IWiaDevMgr;
import jp.chang.wia.WiaDevMgr;
import jp.chang.wia.IWiaDevMgr2;
import jp.chang.wia.WiaDevMgr2;
import jp.chang.wia.EnumWIA_DEV_INFO;
import jp.chang.wia.WiaPropertyStorage;
import jp.chang.wia.IWiaPropertyStorage;
import jp.chang.wia.WiaConsts;
import jp.chang.wia.PROPSPEC;
import jp.chang.wia.PROPVARIANT;
import jp.chang.wia.MyOle32;
import jp.chang.wia.WiaItem;
import jp.chang.wia.WiaItem2;
import jp.chang.wia.EnumWiaItem;
import jp.chang.wia.EnumWiaItem2;
import jp.chang.wia.IWiaDataTransfer;
import jp.chang.wia.WiaDataTransfer;
import jp.chang.wia.STGMEDIUM;
import jp.chang.wia.Wia;
import jp.chang.wia.WiaTypes.PROPID;
import jp.chang.wia.WiaDataCallback;
import jp.chang.wia.WiaDataCallbackImpl;
import jp.chang.wia.WiaDevMgr;
import jp.chang.wia.PropValue;
import jp.chang.wia.EnumSTATPROPSTG;
import jp.chang.wia.STATPROPSTG;

import jp.chang.wia.WindowsVersion;

import java.io.File;
import java.util.List;

public class App 
{

    public static void main( String[] args )
    {
        if( args.length == 0 ){
            System.err.println("usage: App COMMAND");
            System.err.println("COMMAND is one of");
            System.err.println("get-image");
            System.exit(1);
        }
        {
            HRESULT hr = Ole32.INSTANCE.CoInitialize(null);
            if( !(hr.equals(WinError.S_OK) || hr.equals(WinError.S_FALSE)) ){
                throw new RuntimeException("CoInitialize failed");
            }
        }
        String cmd = args[0];
        switch(cmd){
            case "get-image": {
                WiaDevMgr devMgr = Wia.createWiaDevMgr();
                HWND hwnd = Kernel32.INSTANCE.GetConsoleWindow();
                IID iidFormat = new IID(IID_NULL);
                HRESULT hr = devMgr.GetImageDlg(hwnd, 
                    new LONG(WiaConsts.StiDeviceTypeScanner),
                    new LONG(0),
                    new LONG(WiaConsts.WIA_INTENT_NONE),
                    null,
                    new BSTR("image.jpeg"),
                    iidFormat
                );
                COMUtils.checkRC(hr);
                devMgr.Release();
                break;
            }
            case "list-devices": {
                List<Wia.Device> devices = Wia.listDevices();
                devices.stream().forEach(dev -> {
                    System.out.println(dev);
                });
                break;
            }
            case "select-device": {
                WiaDevMgr devMgr = Wia.createWiaDevMgr();
                HWND hwnd = Kernel32.INSTANCE.GetConsoleWindow();
                PointerByReference pp = new PointerByReference();
                HRESULT hr = devMgr.SelectDeviceDlgID(hwnd, new LONG(WiaConsts.StiDeviceTypeScanner), 
                    new LONG(WiaConsts.WIA_SELECT_DEVICE_NODEFAULT), pp);
                COMUtils.checkRC(hr);
                BSTR bstr = new BSTR(pp.getValue());
                System.out.println(bstr);
                devMgr.Release();
                break;
            }
            case "create-device": {
                WiaDevMgr devMgr = Wia.createWiaDevMgr();
                HWND hwnd = Kernel32.INSTANCE.GetConsoleWindow();
                PointerByReference pp = new PointerByReference();
                HRESULT hr = devMgr.SelectDeviceDlgID(hwnd, new LONG(WiaConsts.StiDeviceTypeScanner), 
                    new LONG(WiaConsts.WIA_SELECT_DEVICE_NODEFAULT), pp);
                COMUtils.checkRC(hr);
                BSTR bstr = new BSTR(pp.getValue());
                hr = devMgr.CreateDevice(bstr, pp);
                COMUtils.checkRC(hr);
                WiaItem item = new WiaItem(pp.getValue());
                System.out.println(item);
                item.Release();
                devMgr.Release();
                break;
            }
            case "device-dialog": {
                WiaDevMgr devMgr = Wia.createWiaDevMgr();
                HWND hwnd = Kernel32.INSTANCE.GetConsoleWindow();
                PointerByReference pp = new PointerByReference();
                HRESULT hr = devMgr.SelectDeviceDlgID(hwnd, new LONG(WiaConsts.StiDeviceTypeScanner), 
                    new LONG(WiaConsts.WIA_SELECT_DEVICE_NODEFAULT), pp);
                COMUtils.checkRC(hr);
                BSTR bstr = new BSTR(pp.getValue());
                hr = devMgr.CreateDevice(bstr, pp);
                COMUtils.checkRC(hr);
                WiaItem item = new WiaItem(pp.getValue());
                {
                    IntByReference pCount = new IntByReference();
                    PointerByReference ppItems = new PointerByReference();
                    hr = item.DeviceDlg(hwnd, new LONG(0), new LONG(WiaConsts.WIA_INTENT_NONE),
                        pCount, ppItems);
                    COMUtils.checkRC(hr);
                    int count = pCount.getValue();
                    Pointer[] itemPointers = ppItems.getValue().getPointerArray(0, count);
                    Pointer pItems = ppItems.getValue();
                    for(int i=0;i<count;i++){
                        WiaItem dlogItem = new WiaItem(itemPointers[i]);
                        System.out.println(dlogItem);
                        dlogItem.Release();
                    }
                }
                item.Release();
                devMgr.Release();
                break;
            }
            case "properties": {
                String deviceId = pickDevice();
                WiaPropertyStorage storage = getPropertyStorage(deviceId);
                if( storage == null ){
                    System.out.println("cannot get storage");
                    System.exit(1);
                }
                System.out.println(deviceId);
                ULONGByReference pCount = new ULONGByReference();
                HRESULT hr = storage.GetCount(pCount);
                COMUtils.checkRC(hr);
                int count = pCount.getValue().intValue();
                System.out.println("count: " + count);
                PointerByReference pEnum = new PointerByReference();
                hr = storage.Enum(pEnum);
                COMUtils.checkRC(hr);
                EnumSTATPROPSTG enumSTAT = new EnumSTATPROPSTG(pEnum.getValue());
                {
                    STATPROPSTG stg = new STATPROPSTG();
                    STATPROPSTG[] stages = (STATPROPSTG[])stg.toArray(3);
                    ULONGByReference nFetched = new ULONGByReference();
                    do{
                        hr = enumSTAT.Next(new ULONG(3), stages, nFetched);
                        COMUtils.checkRC(hr);
                        int nn = nFetched.getValue().intValue();
                        for(int i=0;i<nn;i++){
                            System.out.printf("%s (%s): ", stages[i].lpwstrName.toString(), stages[i].propid);
                            PropValue[] values = Wia.readProps(storage, new int[]{ stages[i].propid.intValue() });
                            PropValue propValue = values[0];
                            switch(propValue.getType()){
                                case INT: {
                                    System.out.printf("%d", propValue.getInt());
                                    break;
                                }
                                case STRING: {
                                    System.out.printf("%s", propValue.getString());
                                    break;
                                }
                                default: {
                                    System.out.printf("(unknown value)");
                                    break;
                                }
                            }
                            System.out.println();
                        }
                    } while(nFetched.getValue().intValue() == 3 );
                }
                enumSTAT.Release();
                storage.Release();
                break;
            }
            default: {
                System.err.println("unknown command: " + cmd);
                System.exit(1);
            }
        }
        // if( false ){
        // 	if( WindowsVersion.isXP() ){
        // 		doXP();
        // 	} else {
        // 		doWin10();
        // 	}
        // }
        // if( false ){
        //     testWiaDataCallback();
        // }
        // if( true ){
        //     List<Wia.Device> devices = Wia.listDevices();
        //     System.out.println(devices);
        // }
    }

    static String pickDevice(){
        WiaDevMgr devMgr = Wia.createWiaDevMgr();
        HWND hwnd = Kernel32.INSTANCE.GetConsoleWindow();
        PointerByReference pp = new PointerByReference();
        HRESULT hr = devMgr.SelectDeviceDlgID(hwnd, new LONG(WiaConsts.StiDeviceTypeScanner), 
            new LONG(WiaConsts.WIA_SELECT_DEVICE_NODEFAULT), pp);
        COMUtils.checkRC(hr);
        BSTR bstr = new BSTR(pp.getValue());
        devMgr.Release();
        return bstr.toString();
    }

    static WiaPropertyStorage getPropertyStorage(String deviceId){
        WiaDevMgr wiaDevMgr = Wia.createWiaDevMgr();
        PointerByReference pp = new PointerByReference();
        HRESULT hr = wiaDevMgr.EnumDeviceInfo(WiaConsts.WIA_DEVINFO_ENUM_LOCAL, pp);
        COMUtils.checkRC(hr);
        EnumWIA_DEV_INFO wiaDevInfo = new EnumWIA_DEV_INFO(pp.getValue());
        WiaPropertyStorage ret = new WiaPropertyStorage();
        while(true){
            WiaPropertyStorage.ByReference[] storages = new WiaPropertyStorage.ByReference[1];
            IntByReference nFetched = new IntByReference();
            hr = wiaDevInfo.Next(new ULONG(1), storages, nFetched);
            COMUtils.checkRC(hr);
            if( nFetched.getValue() > 0 ){
                WiaPropertyStorage.ByReference storage = storages[0];
                PropValue[] values = Wia.readProps(storage, new int[]{ WiaConsts.WIA_DIP_DEV_ID });
                String id = values[0].getString();
                if( deviceId.equals(id) ){
                    ret = storage;
                    break;
                }
                storage.Release();
            }
            if( hr.equals(WinError.S_FALSE) ){
                break;
            }
        }
        wiaDevInfo.Release();
        wiaDevMgr.Release();
        return ret;
    }

    static WiaItem getDeviceWiaItem(String deviceId){
        WiaDevMgr devMgr = Wia.createWiaDevMgr();
        PointerByReference pp = new PointerByReference();
        HRESULT hr = devMgr.CreateDevice(new BSTR(deviceId), pp);
        WiaItem item = new WiaItem(pp.getValue());
        devMgr.Release();
        return item;
    }

    static void doWin10(){
        doXP();
    	// HRESULT hr;
    	// Ole32.INSTANCE.CoInitialize(null);
     //    PointerByReference pp = new PointerByReference();
     //    hr = Ole32.INSTANCE.CoCreateInstance(WiaConsts.CLSID_WiaDevMgr2, null, 
     //        WTypes.CLSCTX_LOCAL_SERVER, IWiaDevMgr2.IID_IWiaDevMgr2, pp);
     //    COMUtils.checkRC(hr);
     //    WiaDevMgr2 wiaDevMgr2 = new WiaDevMgr2(pp.getValue());
     //    EnumWIA_DEV_INFO wiaDevInfo = wiaDevMgr2.EnumDeviceInfo(WiaConsts.WIA_DEVINFO_ENUM_LOCAL);
     //    System.out.println(wiaDevInfo.GetCount());
     //    WiaPropertyStorage.ByReference[] storages = new WiaPropertyStorage.ByReference[1];
     //    long nFetched = wiaDevInfo.Next(1, storages);
     //    System.out.printf("fetched: %d\n", nFetched);
     //    if( nFetched <= 0 ){
     //    	return;
     //    }
     //    PROPSPEC[] propspecs = new PROPSPEC[1];
     //    PROPVARIANT onePROPVARIANT = new PROPVARIANT();
     //    Memory propvars = new Memory(onePROPVARIANT.size()*1);
     //    {
     //        PROPSPEC prop = new PROPSPEC();
     //        prop.kind = new ULONG(PROPSPEC.PRSPEC_PROPID);
     //        prop.value.setType(ULONG.class);
     //        prop.value.propid = new ULONG(WiaConsts.WIA_DIP_DEV_ID);
     //        propspecs[0] = prop;
     //    }
     //    propvars.clear();
     //    WiaPropertyStorage.ByReference storage = storages[0];
     //    hr = storage.ReadMultiple(1, propspecs, propvars);
     //    System.out.printf("0x%x\n", hr.intValue());
     //    COMUtils.checkRC(hr);
     //    PROPVARIANT one = new PROPVARIANT();
     //    short vt = PROPVARIANT.getVt(propvars, 0);
     //    System.out.println(vt);
     //    if( vt != 8 ){
     //        throw new RuntimeException("cannot get device ID");
     //    }
     //    BSTR deviceID = PROPVARIANT.getBSTR(propvars, 0);
     //    System.out.println(deviceID.getValue());
     //    WiaItem2 wiaItem2 = wiaDevMgr2.CreateDevice(deviceID);
     //    System.out.printf("itemType: 0x%x\n", wiaItem2.GetItemType());
     //    hr = MyOle32.INSTANCE.FreePropVariantArray(new ULONG(1), propvars);
     //    {
     //        EnumWiaItem2 enumWiaItem = wiaItem2.EnumChildItems(null);
     //        WiaItem2.ByReference[] refs = new WiaItem2.ByReference[3];
     //        IntByReference nAvailable = new IntByReference();
     //        hr = enumWiaItem.Next(new ULONG(3), refs, nAvailable);
     //        System.out.printf("hr: 0x%x\n", hr.intValue());
     //        COMUtils.checkRC(hr);
     //        System.out.printf("nAvailable: %d\n", nAvailable.getValue());
     //        if( nAvailable.getValue() > 0 ){
     //            WiaItem2.ByReference item2 = refs[0];
     //            int type = item2.GetItemType();
     //            System.out.printf("child item type: 0x%x\n", type);
     //        }
     //    }
    }

    static void doXP(){
    	// Ole32.INSTANCE.CoInitialize(null);
    	// CLSID.ByReference CLSID_WiaDevMgr = new CLSID.ByReference();
    	// HRESULT hr = Ole32.INSTANCE.CLSIDFromProgID("WiaDevMgr", CLSID_WiaDevMgr);
     //    COMUtils.checkRC(hr);
     //    PointerByReference pp = new PointerByReference();
     //    hr = Ole32.INSTANCE.CoCreateInstance(CLSID_WiaDevMgr, null, 
     //        WTypes.CLSCTX_LOCAL_SERVER, IWiaDevMgr.IID_IWiaDevMgr, pp);
     //    COMUtils.checkRC(hr);
     //    WiaDevMgr wiaDevMgr = new WiaDevMgr(pp.getValue());
     //    EnumWIA_DEV_INFO wiaDevInfo = wiaDevMgr.EnumDeviceInfo(WiaConsts.WIA_DEVINFO_ENUM_LOCAL);
     //    System.out.println(wiaDevInfo.GetCount());
     //    WiaPropertyStorage.ByReference[] storages = new WiaPropertyStorage.ByReference[1];
     //    long nFetched = wiaDevInfo.Next(1, storages);
     //    System.out.printf("fetched: %d\n", nFetched);
     //    if( nFetched > 0 ){
     //        PROPSPEC[] propspecs = new PROPSPEC[1];
     //        PROPVARIANT[] propvars = new PROPVARIANT[1];
     //        {
     //            PROPSPEC prop = new PROPSPEC();
     //            prop.kind = new ULONG(PROPSPEC.PRSPEC_PROPID);
     //            prop.value.setType(ULONG.class);
     //            prop.value.propid = new ULONG(WiaConsts.WIA_DIP_DEV_ID);
     //            propspecs[0] = prop;
     //        }
     //        WiaPropertyStorage.ByReference storage = storages[0];
     //        hr = storage.ReadMultiple(1, propspecs, propvars);
     //        System.out.printf("0x%x\n", hr.intValue());
     //        COMUtils.checkRC(hr);
     //        int vt = propvars[0].getVt();
     //        System.out.println(vt);
     //        if( vt != 8 ){
     //            throw new RuntimeException("cannot get device ID");
     //        }
     //        BSTR deviceID = propvars[0].getUnionBSTR();
     //        System.out.println(deviceID.getValue());
     //        WiaItem wiaItem = wiaDevMgr.CreateDevice(deviceID);
     //        System.out.printf("itemType: 0x%x\n", wiaItem.GetItemType());
     //        hr = MyOle32.INSTANCE.FreePropVariantArray(new ULONG(1), propvars);
     //        COMUtils.checkRC(hr);
     //        {
     //            EnumWiaItem enumWiaItem = wiaItem.EnumChildItems();
     //            WiaItem.ByReference[] refs = new WiaItem.ByReference[3];
     //            IntByReference nAvailable = new IntByReference();
     //            hr = enumWiaItem.Next(3, refs, nAvailable);
     //            System.out.printf("hr: 0x%x\n", hr.intValue());
     //            COMUtils.checkRC(hr);
     //            System.out.printf("nAvailable: %d\n", nAvailable.getValue());
     //            if( nAvailable.getValue() > 0 ){
     //                WiaItem.ByReference item = refs[0];
     //                int type = item.GetItemType();
     //                System.out.printf("child item type: 0x%x\n", type);
     //            }
     //        }
     //        if( true ){
     //            IntByReference count = new IntByReference();
     //            PointerByReference items = new PointerByReference();
     //            hr = wiaItem.DeviceDlg(Kernel32.INSTANCE.GetConsoleWindow(), new LONG(0),
     //                new LONG(0), count, items);
     //            COMUtils.checkRC(hr);
     //            if( count.getValue() > 0 ){
     //                WiaItem item = new WiaItem(items.getValue().getPointer(0));
     //                System.out.printf("scan item type: 0x%x\n", item.GetItemType());
     //                {
     //                    System.out.printf("sizeof PROPVARIANT: %d\n", new PROPVARIANT().size());
     //                    PROPSPEC[] props = (PROPSPEC[])new PROPSPEC().toArray(2);
     //                    PROPVARIANT[] variants = (PROPVARIANT[])new PROPVARIANT().toArray(2);
     //                    System.out.println(props[0]);
     //                    props[0].kind = new ULONG(PROPSPEC.PRSPEC_PROPID);
     //                    props[0].value.setType(ULONG.class);
     //                    props[0].value.propid = new ULONG(WiaConsts.WIA_IPA_FORMAT);
     //                    props[1].kind = new ULONG(PROPSPEC.PRSPEC_PROPID);
     //                    props[1].value.setType(ULONG.class);
     //                    props[1].value.propid = new ULONG(WiaConsts.WIA_IPA_TYMED);
     //                    variants[0].vt = new VARTYPE(Variant.VT_CLSID);
     //                    variants[0].value.setType(Pointer.class);
     //                    variants[0].value.pointerValue = new CLSID(WiaConsts.WiaImgFmt_BMP).getPointer();
     //                    variants[1].vt = new VARTYPE(Variant.VT_I4);
     //                    variants[1].value.setType(LONG.class);
     //                    variants[1].value.lVal = new LONG(WiaConsts.TYMED_FILE);
     //                    PointerByReference pbr = new PointerByReference();
     //                    hr = item.QueryInterface(new REFIID(IWiaPropertyStorage.IID_IWiaPropertyStorage), pbr);
     //                    COMUtils.checkRC(hr);
     //                    WiaPropertyStorage propStorage = new WiaPropertyStorage(pbr.getValue());
     //                    hr = propStorage.WriteMultiple(new ULONG(2), props, variants, 
     //                        new PROPID(WiaConsts.WIA_RESERVED_FOR_NEW_PROPS));
     //                    COMUtils.checkRC(hr);
     //                }
     //                PointerByReference ptr = new PointerByReference();
     //                hr = item.QueryInterface(new REFIID(IWiaDataTransfer.IID_IWiaDataTransfer), ptr);
     //                COMUtils.checkRC(hr);
     //                System.out.println(ptr.getValue());
     //                WiaDataTransfer wiaDataTransfer = new WiaDataTransfer(ptr.getValue());
     //                STGMEDIUM stgmedium = new STGMEDIUM();
     //                stgmedium.tymed = new DWORD(WiaConsts.TYMED_FILE);
     //                stgmediumGunionValue.setType(Pointer.class);
     //                String fileName = System.getenv("HOME") + File.separator + "scan.bmp";
     //                stgmedium.unionValue.pointer = new LPOLESTR(fileName).getPointer();
     //                WiaDataCallback dataCallback = WiaDataCallbackImpl.create(new WiaDataCallbackImpl.BandedDataCallbackCallback(){
     //                    @Override
     //                    public HRESULT invoke(Pointer thisPointer, LONG lMessage, LONG lStatus, LONG lPercentComplete,
     //                        LONG lOffset, LONG lLength, LONG lReserved, LONG lResLength, PointerByReference pbBuffer){
     //                        System.out.printf("callback %d\n", lPercentComplete.intValue());
     //                        return WinError.S_OK;
     //                    };
     //                });
     //                hr = wiaDataTransfer.idtGetData(stgmedium, dataCallback);
     //                System.out.printf("0x%x\n", hr.intValue());
     //            }
     //        }
     //    }
    }

    private static void testWiaDataCallback(){
        WiaDataCallback cb = WiaDataCallbackImpl.create(new WiaDataCallbackImpl.BandedDataCallbackCallback(){
            @Override
            public HRESULT invoke(Pointer thisPointer, LONG lMessage, LONG lStatus, LONG lPercentComplete,
                LONG lOffset, LONG lLength, LONG lReserved, LONG lResLength, PointerByReference pbBuffer){
                return WinError.S_OK;
            }
        });
        System.out.println(cb.AddRef());
        System.out.println(cb.Release());
        System.out.println(cb.Release());
    }
}
