package jp.chang.myclinic;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.EventQueue;

class ScanTask implements Runnable {

	private ScannerDialog dialog;
	private String deviceId;
	private Path savePath;
	private int resolution;
	private boolean canceled;
	private static Logger logger = LoggerFactory.getLogger(TaskScan.class);

	public ScanTask(ScannerDialog dialog, String deviceId, Path savePath){
		this.dialog = dialog;
		this.deviceId = deviceId;
		this.savePath = savePath;
		this.resolution = ScannerSetting.INSTANCE.dip;
		this.canceled = false;
	}

	public void setCanceled(boolean canceled){
		this.canceled = canceled;
	}

	@Override
	public void run(){
		WiaItem deviceItem = null;
		WiaItem wiaItem = null;
		WiaDataTransfer wiaTransfer = null;
		WiaDataCallback dataCallback = null;
		try{
			Wia.CoInitialize();
			deviceItem = getDeviceWiaItem(deviceId);
			if( deviceItem == null ){
				reportError("スキャナーが見つかりません。", null);
				return;
			}
        	wiaItem = findScannerFile(deviceItem);
        	if( wiaItem == null ){
        		reportError("スキャンを開始できません。", null);
        		return;
        	}
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
            wiaTransfer = transfer;
            STGMEDIUM stgmedium = new STGMEDIUM();
            stgmedium.tymed = new DWORD(WiaConsts.TYMED_FILE);
            stgmedium.unionValue.setType(Pointer.class);
            String fileName = savePath.toString();
            stgmedium.unionValue.pointer = new LPOLESTR(fileName).getPointer();
            dataCallback = WiaDataCallbackImpl.create(new WiaDataCallbackImpl.BandedDataCallbackCallback(){
                @Override
                public HRESULT invoke(Pointer thisPointer, LONG lMessage, LONG lStatus, LONG lPercentComplete,
                    LONG lOffset, LONG lLength, LONG lReserved, LONG lResLength, PointerByReference pbBuffer){
                	try{
	                    if( canceled ){
	                    	logger.debug("invoke returns S_FALSE");
	                    	return WinError.S_FALSE;
	                    }
	                    int message = lMessage.intValue();
	                    if( message == WiaConsts.IT_MSG_DATA || message == WiaConsts.IT_MSG_STATUS ){
		                	int pct = lPercentComplete.intValue();
		                	reportProgress(pct);
	                    }
                    	logger.debug("invoke returns S_OK");
	                    return WinError.S_OK;
	                } catch(Exception ex){
	                	reportError("スキャン中にエラーが発生しました(2)。", ex);
                    	logger.debug("invoke returns S_FALSE");
	                	return WinError.S_FALSE;
	                }
                };
            });
            hr = transfer.idtGetData(stgmedium, dataCallback);
            logger.info("idtGetData returned {}", hr);
            if( canceled ){
            	try{
            		Files.deleteIfExists(savePath);
            	} catch(IOException ex){
            		logger.error("failed to delete file: {}", savePath, ex);
            	}
            }
            COMUtils.checkRC(hr);
			reportSuccess();
		} catch(Exception ex){
			reportError("スキャン中にエラーが発生しました(1)。", ex);
		} finally{
			if( dataCallback != null ){
				dataCallback.Release();
			}
			if( wiaTransfer != null ){
				wiaTransfer.Release();
			}
			if( wiaItem != null ){
				wiaItem.Release();
			}
			if( deviceItem != null ){
				deviceItem.Release();
			}
		}
	}

	private void reportSuccess(){
		EventQueue.invokeLater(() -> {
			dialog.onSuccess();
		});
	}

	private void reportError(String message, Exception ex){
		setCanceled(true);
		EventQueue.invokeLater(() -> {
			dialog.onError(message, ex);
		});
	}

	private void reportProgress(int percent){
		EventQueue.invokeLater(() -> {
			dialog.onProgress(percent);
		});
	}

    private static WiaItem getDeviceWiaItem(String deviceId){
        WiaDevMgr devMgr = Wia.createWiaDevMgr();
        PointerByReference pp = new PointerByReference();
        HRESULT hr = devMgr.CreateDevice(new BSTR(deviceId), pp);
        WiaItem item = null;
        if( hr.intValue() == COMUtils.S_OK ){
        	item = new WiaItem(pp.getValue());
        }
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
        COMUtils.checkRC(hr);
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
        enumWiaItem.Release();
        return wiaItem;
    }

}