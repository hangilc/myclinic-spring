package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

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
    private int lastPageIndex;
    private String timeStamp;
    private Path saveDir;
    private JLabel numPagesLabel;
    private JLabel previewImageLabel;
    private List<Path> savedPages;
    private int previewIndex;
    private JLabel previewStatusLabel;
    private static DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");

    public PatientDocScanner(Frame owner, int patientId){
        super(owner, "患者書類のスキャン", true);
        this.patientId = patientId;
        this.lastPageIndex = 0;
        this.timeStamp = makeTimeStamp();
        this.saveDir = getSaveDir();
        this.savedPages = new ArrayList<Path>();
        this.numPagesLabel = new JLabel(String.valueOf(savedPages.size()));
        this.previewIndex = -1;
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
        panel.add(makePreviewPanel());
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

    private JComponent makePreviewPanel(){
    	JPanel panel = new JPanel();
    	{
    		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
    		panel.setLayout(layout);
    	}
    	previewImageLabel = new JLabel("PREVIEW");
    	Dimension dim = new Dimension(210, 297);
    	previewImageLabel.setMinimumSize(dim);
    	previewImageLabel.setPreferredSize(dim);
    	previewImageLabel.setMaximumSize(dim);
    	panel.add(previewImageLabel);
    	panel.add(makePreviewControlPanel());
    	return panel;
    }

    private JComponent makePreviewControlPanel(){
    	JPanel panel = new JPanel();
    	{
    		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
    		panel.setLayout(layout);
    	}
    	{
    		previewStatusLabel = new JLabel("0/0");
    		panel.add(previewStatusLabel);
    	}
    	{
    		JPanel navPanel = new JPanel();
    		{
	    		BoxLayout layout = new BoxLayout(navPanel, BoxLayout.X_AXIS);
	    		navPanel.setLayout(layout);
    		}
    		JButton prevButton = new JButton("前へ");
    		prevButton.addActionListener(event -> {
    			int index = previewIndex - 1;
    			if( index >= 0 && index < savedPages.size() ){
    				previewIndex = index;
    				updatePreview();
    			}
    		});
    		navPanel.add(prevButton);
    		JButton nextButton = new JButton("次へ");
    		nextButton.addActionListener(event -> {
    			int index = previewIndex + 1;
    			if( index >= 0 && index < savedPages.size() ){
    				previewIndex = index;
    				updatePreview();
    			}
    		});
    		navPanel.add(nextButton);
    		panel.add(navPanel);
    	}
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

    private void incLastPageIndex(){
    	lastPageIndex += 1;
    	numPagesLabel.setText("" + savedPages.size());
    	pack();
    }

    private void updatePreview(){
    	updatePreviewImage();
    	updatePreviewStatus();
    }

    private void updatePreviewImage(){
    	if( previewIndex < 0 ){
    		return;
    	}
    	Path path = savedPages.get(previewIndex);
    	BufferedImage origImg;
    	try{
	    	origImg = ImageIO.read(path.toFile());
	    } catch(IOException ex){
	    	throw new UncheckedIOException(ex);
	    }
    	int type = origImg.getType();
    	if( type == 0 ){
    		type = BufferedImage.TYPE_INT_ARGB;
    	}
    	Dimension dim = previewImageLabel.getSize(null);
    	BufferedImage resizedImg = new BufferedImage(dim.width, dim.height, type);
    	Graphics2D g = resizedImg.createGraphics();
    	g.drawImage(origImg, 0, 0, dim.width, dim.height, null);
    	g.dispose();
    	previewImageLabel.setIcon(new ImageIcon(resizedImg));
    }

    private void updatePreviewStatus(){
    	if( previewIndex < 0 ){
    		previewStatusLabel.setText("0/0");
    	} else {
    		previewStatusLabel.setText((previewIndex + 1) + "/" + savedPages.size());
    	}
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
                String saveFileName = String.format("%d-%s-%02d.bmp", patientId, timeStamp, lastPageIndex+1);
                Path savePath = saveDir.resolve(saveFileName);
        		final WiaItem scanWiaItem = wiaItem;
                WiaPropertyStorage storage = Wia.getStorageForWiaItem(scanWiaItem);
                {
                    PropValue[] pvals = Wia.readProps(storage, new int[]{ WiaConsts.WIA_IPS_XRES });
                    System.out.println(pvals[0].getInt());
                }
                {
                    PROPSPEC[] props = (PROPSPEC[])new PROPSPEC().toArray(2);
                    PROPVARIANT[] variants = (PROPVARIANT[])new PROPVARIANT().toArray(2);
                    props[0].kind = new ULONG(PROPSPEC.PRSPEC_PROPID);
                    props[0].value.setType(ULONG.class);
                    props[0].value.propid = new ULONG(WiaConsts.WIA_IPA_FORMAT);
                    props[1].kind = new ULONG(PROPSPEC.PRSPEC_PROPID);
                    props[1].value.setType(ULONG.class);
                    props[1].value.propid = new ULONG(WiaConsts.WIA_IPA_TYMED);
                    variants[0].vt = new VARTYPE(Variant.VT_CLSID);
                    variants[0].value.setType(Pointer.class);
                    variants[0].value.pointerValue = new CLSID(WiaConsts.WiaImgFmt_BMP).getPointer();
                    variants[1].vt = new VARTYPE(Variant.VT_I4);
                    variants[1].value.setType(LONG.class);
                    variants[1].value.lVal = new LONG(WiaConsts.TYMED_FILE);
                    PointerByReference pbr = new PointerByReference();
                    HRESULT hr = wiaItem.QueryInterface(new REFIID(IWiaPropertyStorage.IID_IWiaPropertyStorage), pbr);
                    COMUtils.checkRC(hr);
                    WiaPropertyStorage propStorage = new WiaPropertyStorage(pbr.getValue());
                    hr = storage.WriteMultiple(new ULONG(2), props, variants, 
                        new PROPID(WiaConsts.WIA_RESERVED_FOR_NEW_PROPS));
                    COMUtils.checkRC(hr);
                }
                Wia.writeResolution(storage, 300);
                storage.Release();
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
	            if( !dialog.isCanceled() ){
	            	final Path outPath = convertImage(savePath, "jpg");
	            	savedPages.add(outPath);
	            	previewIndex = savedPages.size() - 1;
		            EventQueue.invokeLater(() -> {
			            this.incLastPageIndex();
			            updatePreviewImage();
			            dialog.dispose();
		            });
		        } else {
		        	try{
		        		System.out.println("Deleting: " + savePath);
			        	Files.deleteIfExists(savePath);
			        } catch(IOException ex){
			        	throw new UncheckedIOException(ex);
			        }
			        EventQueue.invokeLater(() -> {
			        	dialog.dispose();
			        });
		        }
        	}
    		deviceItem.Release();
    	}).start();
    	dialog.setVisible(true);
    }

    private Path convertImage(Path source, String format){
    	try{
	    	BufferedImage src = ImageIO.read(source.toFile());
	    	String srcFileName = source.getFileName().toString();
	    	String dstFileName = srcFileName.replaceFirst("\\.bmp$", "." + format);
	    	Path output = source.resolveSibling(dstFileName);
	    	System.out.println(output.toString());
	    	boolean ok = ImageIO.write(src, format, output.toFile());
	    	if( !ok ){
	    		throw new RuntimeException("image conversion failed");
	    	}
            Files.delete(source);
            return output;
	    } catch(IOException ex){
	    	throw new UncheckedIOException(ex);
	    }
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