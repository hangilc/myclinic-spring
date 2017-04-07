package jp.chang.myclinic;

import java.nio.file.Path;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Guid.CLSID;
import com.sun.jna.platform.win32.Guid.REFIID;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinDef.LONGByReference;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinDef.ULONGByReference;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WTypes.LPOLESTR;
import com.sun.jna.ptr.PointerByReference;

import jp.chang.wia.EnumWiaItem;
import jp.chang.wia.IWiaDataTransfer;
import jp.chang.wia.PropertyWriter;
import jp.chang.wia.STGMEDIUM;
import jp.chang.wia.Wia;
import jp.chang.wia.WiaConsts;
import jp.chang.wia.WiaDataCallback;
import jp.chang.wia.WiaDataCallbackImpl;
import jp.chang.wia.WiaDataTransfer;
import jp.chang.wia.WiaDevMgr;
import jp.chang.wia.WiaItem;
import jp.chang.wia.WiaPropertyStorage;

class TaskScan implements Runnable {

	private String deviceId;
	private Path savePath;
	private int resolution;
	private boolean canceled;

	public TaskScan(String deviceId, Path savePath){
		this.deviceId = deviceId;
		this.savePath = savePath;
		this.resolution = 300;
		this.canceled = false;
	}

	public boolean isCanceled(){
		return canceled;
	}

	public void setCanceled(boolean canceled){
		this.canceled = canceled;
	}

	@Override
	public void run(){
		Wia.CoInitialize();
		WiaItem deviceItem = getDeviceWiaItem(deviceId);
		if( deviceItem == null ){
			onFail("スキャナーが見つかりません。");
		} else {
        	WiaItem wiaItem = findScannerFile(deviceItem);
        	if( wiaItem == null ){
        		onFail("スキャンを開始できません。");
        	} else {
        		final WiaItem scanWiaItem = wiaItem;
                new PropertyWriter()
                	.set(WiaConsts.WIA_IPA_FORMAT, new CLSID(WiaConsts.WiaImgFmt_BMP))
                	.set(WiaConsts.WIA_IPA_TYMED, new LONG(WiaConsts.TYMED_FILE))
                	.set(WiaConsts.WIA_IPS_XRES, new LONG(resolution))
                	.set(WiaConsts.WIA_IPS_YRES, new LONG(resolution))
                	.write(wiaItem);	
	            PointerByReference pWiaDataTransfer = new PointerByReference();
	            HRESULT hr = scanWiaItem.QueryInterface(new REFIID(IWiaDataTransfer.IID_IWiaDataTransfer), pWiaDataTransfer);
	            COMUtils.checkRC(hr);
	            final WiaDataTransfer transfer = new WiaDataTransfer(pWiaDataTransfer.getValue());
	            STGMEDIUM stgmedium = new STGMEDIUM();
	            stgmedium.tymed = new DWORD(WiaConsts.TYMED_FILE);
	            stgmedium.unionValue.setType(Pointer.class);
	            String fileName = savePath.toString();
	            stgmedium.unionValue.pointer = new LPOLESTR(fileName).getPointer();
	            WiaDataCallback dataCallback = WiaDataCallbackImpl.create(new WiaDataCallbackImpl.BandedDataCallbackCallback(){
	                @Override
	                public HRESULT invoke(Pointer thisPointer, LONG lMessage, LONG lStatus, LONG lPercentComplete,
	                    LONG lOffset, LONG lLength, LONG lReserved, LONG lResLength, PointerByReference pbBuffer){
	                    onCallback();
	                    if( canceled ){
	                    	return WinError.S_FALSE;
	                    }
	                    int message = lMessage.intValue();
	                    if( message == WiaConsts.IT_MSG_DATA || message == WiaConsts.IT_MSG_STATUS ){
		                	int pct = lPercentComplete.intValue();
		                	onProgress(pct);
	                    }
	                    return WinError.S_OK;
	                };
	            });
	            hr = transfer.idtGetData(stgmedium, dataCallback);
	            COMUtils.checkRC(hr);
	            dataCallback.Release();
	            transfer.Release();
	            onFinish();
        	}
			deviceItem.Release();
		}
	}

	public void onFail(String message){

	}

	public void onCallback(){

	}

	public void onProgress(int percent){
        System.out.printf("callback %d\n", percent);
	}

	public void onFinish(){

	}

    private static WiaItem getDeviceWiaItem(String deviceId){
        WiaDevMgr devMgr = Wia.createWiaDevMgr();
        PointerByReference pp = new PointerByReference();
        HRESULT hr = devMgr.CreateDevice(new BSTR(deviceId), pp);
        WiaItem item = new WiaItem(pp.getValue());
        devMgr.Release();
        return item;
    }

    private static WiaItem findScannerFile(WiaItem deviceItem){
        WiaItem wiaItem = null;
        PointerByReference pIEnumWiaItem = new PointerByReference();
        HRESULT hr = deviceItem.EnumChildItems(pIEnumWiaItem);
        COMUtils.checkRC(hr);
        EnumWiaItem enumWiaItem = new EnumWiaItem(pIEnumWiaItem.getValue());
        WiaItem.ByReference[] items = new WiaItem.ByReference[4];
        ULONGByReference pFetched = new ULONGByReference();
        hr = enumWiaItem.Next(new ULONG(4), items, pFetched);
        int nFetched = pFetched.getValue().intValue();
        for(int i=0;i<nFetched;i++){
            WiaItem.ByReference item = items[i];
            LONGByReference pItemType = new LONGByReference();
            hr = item.GetItemType(pItemType);
            COMUtils.checkRC(hr);
            int itemType = pItemType.getValue().intValue();
            if( (itemType & WiaConsts.WiaItemTypeImage) != 0 && 
                (itemType & WiaConsts.WiaItemTypeFile) != 0 ){
                wiaItem = item;
            	break;
            } else {
                item.Release();
            }
        }
        COMUtils.checkRC(hr);
        enumWiaItem.Release();
        return wiaItem;
    }

}