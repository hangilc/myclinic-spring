package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
import com.sun.jna.platform.win32.WinDef.LONGByReference;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Paths;
import java.nio.file.Path;

public class PatientDocScanner extends JDialog {

    private int patientId;
    private int numPages = 0;
    private String timeStamp;
    private Path saveDir;
    private JLabel numPagesLabel;
    private static DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");

    public PatientDocScanner(Frame owner, int patientId){
        super(owner, "患者書類のスキャン", true);
        this.patientId = patientId;
        this.timeStamp = makeTimeStamp();
        this.saveDir = getSaveDir();
        this.numPagesLabel = new JLabel(String.valueOf(numPages));
        add(makeCenterPanel(), BorderLayout.CENTER);
        add(makeCommandPanel(), BorderLayout.SOUTH);
        pack();
    }

    private JComponent makeCenterPanel(){
        JPanel panel = new JPanel();
        panel.add(makeCenterPanelContent());
        return panel;
    }

    private JComponent makeCenterPanelContent(){
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(boxLayout);
        JLabel patientIdLabel = new JLabel("患者番号：" + patientId);
        patientIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(patientIdLabel);
        JComponent pagesPanel = makeScannedPagesPanel();
        pagesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(pagesPanel);
        return panel;
    }

    private JComponent makeScannedPagesPanel(){
        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout();
        panel.setLayout(layout);
        JLabel label = new JLabel("スキャンしたページ数：");
        panel.add(label);
        panel.add(numPagesLabel);
        return panel;
    }

    private JComponent makeCommandPanel(){
        JPanel panel = new JPanel();
        JButton start = new JButton("スタート");
        JButton finish = new JButton("終了");
        int maxWidth = 0;
        for(JButton btn: new JButton[]{start, finish}){
            Dimension dim = btn.getPreferredSize();
            if( dim.width > maxWidth ){
                maxWidth = dim.width;
            }
        }
        for(JButton btn: new JButton[]{start, finish}){
            Dimension dim = btn.getPreferredSize();
            dim.width = maxWidth;
            btn.setPreferredSize(dim);
        }
        start.addActionListener(this::doStart);
        finish.addActionListener(event -> {
            dispose();
        });
        panel.add(start);
        panel.add(finish);
        return panel;
    }

    private Path getSaveDir(){
        Path path = ScannerProperties.INSTANCE.getSaveDir();
        if( path == null ){
            path = Paths.get(System.getProperty("user.dir"));
        }
        return path;
    }

    private void incNumPages(){
    	numPages += 1;
    	numPagesLabel.setText("" + numPages);
    	pack();
    }

    private void doStart(ActionEvent event){
    	String deviceId = resolveDeviceId();
    	if( deviceId == null ){
    		return;
    	}
        final ScanProgressDialog dialog = new ScanProgressDialog(this);
        dialog.setLocationByPlatform(true);
    	new Thread(() -> {
    		Wia.CoInitialize();
    		WiaItem deviceItem = getDeviceWiaItem(deviceId);
    		if( deviceItem == null ){
    			return;
    		}
        	WiaItem wiaItem = findScannerFile(deviceItem);
        	if( wiaItem != null ){
        		final WiaItem scanWiaItem = wiaItem;
	            String saveFileName = String.format("%d-%s-%02d.bmp", patientId, timeStamp, numPages+1);
	            Path savePath = saveDir.resolve(saveFileName);
	            PointerByReference pWiaDataTransfer = new PointerByReference();
	            HRESULT hr = scanWiaItem.QueryInterface(new REFIID(IWiaDataTransfer.IID_IWiaDataTransfer), pWiaDataTransfer);
	            COMUtils.checkRC(hr);
	            final WiaDataTransfer transfer = new WiaDataTransfer(pWiaDataTransfer.getValue());
	            STGMEDIUM stgmedium = new STGMEDIUM();
	            stgmedium.tymed = new DWORD(WiaConsts.TYMED_FILE);
	            stgmedium.unionValue.setType(Pointer.class);
	            String fileName = savePath.toString();
	            stgmedium.unionValue.pointer = new LPOLESTR(fileName).getPointer();
	            final boolean[] isCanceled = new boolean[]{ false };
	            WiaDataCallback dataCallback = WiaDataCallbackImpl.create(new WiaDataCallbackImpl.BandedDataCallbackCallback(){
	                @Override
	                public HRESULT invoke(Pointer thisPointer, LONG lMessage, LONG lStatus, LONG lPercentComplete,
	                    LONG lOffset, LONG lLength, LONG lReserved, LONG lResLength, PointerByReference pbBuffer){
	                    if( dialog.isCanceled() ){
	                    	return WinError.S_FALSE;
	                    }
	                    int message = lMessage.intValue();
	                    if( message == WiaConsts.IT_MSG_DATA || message == WiaConsts.IT_MSG_STATUS ){
		                	int pct = lPercentComplete.intValue();
		                    System.out.printf("callback %d\n", pct);
		                    EventQueue.invokeLater(() -> {
			                    dialog.setValue(pct);
		                    });
	                    }
	                    return WinError.S_OK;
	                };
	            });
	            hr = transfer.idtGetData(stgmedium, dataCallback);
	            COMUtils.checkRC(hr);
	            transfer.Release();
	            scanWiaItem.Release();
	            EventQueue.invokeLater(() -> {
	            	dialog.dispose();
	            });
	            if( !dialog.isCanceled() ){
		            EventQueue.invokeLater(() -> {
			            this.incNumPages();
		            });
		        } else {
		        	try{
		        		System.out.println("Deleting: " + savePath);
			        	Files.deleteIfExists(savePath);
			        } catch(IOException ex){
			        	throw new UncheckedIOException(ex);
			        }
		        }
        	}
    		deviceItem.Release();
    	}).start();
    	dialog.setVisible(true);
    }

    private String resolveDeviceId(){
        List<Wia.Device> devices = Wia.listDevices();
    	if( devices.size() == 0 ){
            JOptionPane.showMessageDialog(this, "接続された。スキャナーがみつかりません。");
    		return null;
    	} else if( devices.size() == 1 ){
    		return devices.get(0).deviceId;
    	} else {
    		return pickDevice();
    	}
    }

    private String makeTimeStamp(){
        LocalDateTime dt = LocalDateTime.now();
        return dt.format(timeStampFormatter);
    }

    private static WiaItem getDeviceWiaItem(String deviceId){
        WiaDevMgr devMgr = Wia.createWiaDevMgr();
        PointerByReference pp = new PointerByReference();
        HRESULT hr = devMgr.CreateDevice(new BSTR(deviceId), pp);
        WiaItem item = new WiaItem(pp.getValue());
        devMgr.Release();
        return item;
    }

    private static String pickDevice(){
        WiaDevMgr devMgr = Wia.createWiaDevMgr();
        HWND hwnd = Kernel32.INSTANCE.GetConsoleWindow();
        PointerByReference pp = new PointerByReference();
        HRESULT hr = devMgr.SelectDeviceDlgID(hwnd, new LONG(WiaConsts.StiDeviceTypeScanner), 
            new LONG(WiaConsts.WIA_SELECT_DEVICE_NODEFAULT), pp);
        COMUtils.checkRC(hr);
        if( hr.equals(COMUtils.S_FALSE) ){
            return null;
        } else {
            BSTR bstr = new BSTR(pp.getValue());
            devMgr.Release();
            return bstr.toString();
        }
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
            } else {
                item.Release();
            }
        }
        COMUtils.checkRC(hr);
        enumWiaItem.Release();
        return wiaItem;
    }

}