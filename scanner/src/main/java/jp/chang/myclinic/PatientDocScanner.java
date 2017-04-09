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
    private static DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");
    private PatientDocInfoPanel patientDocInfoPanel;
    private PatientDocPreviewPanel patientDocPreviewPanel;

    public PatientDocScanner(Frame owner, int patientId){
        super(owner, "患者書類のスキャン", true);
        this.patientId = patientId;
        this.lastPageIndex = 0;
        this.timeStamp = makeTimeStamp();
        this.saveDir = getSaveDir();
        this.patientDocInfoPanel = new PatientDocInfoPanel(patientId);
        this.patientDocPreviewPanel = new PatientDocPreviewPanel();
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
        panel.add(patientDocInfoPanel);
        panel.add(patientDocPreviewPanel);
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

    private void addPage(Path path){
        patientDocPreviewPanel.addPage(path);
        patientDocPreviewPanel.update();
        int pages = patientDocPreviewPanel.getNumberOfPages();
        patientDocInfoPanel.updateTotalPages(pages);
    }

    private int getNextPageIndex(){
        return patientDocPreviewPanel.getNumberOfPages() + 1;
    }

    private void doStart(ActionEvent event){
    	String deviceId = resolveDeviceId();
    	if( deviceId == null ){
    		return;
    	}
        final ScanProgressDialog dialog = new ScanProgressDialog(this);
        dialog.setLocationByPlatform(true);
        String saveFileName = String.format("%d-%s-%02d.bmp", patientId, timeStamp, getNextPageIndex());
        Path savePath = saveDir.resolve(saveFileName);
		TaskScan task = new TaskScan(deviceId, savePath){
			@Override
			public void onFail(String message){
				EventQueue.invokeLater(() -> {
		            JOptionPane.showMessageDialog(PatientDocScanner.this, message);
		            dialog.dispose();
				});
			}

			@Override
			public void onFinish(){
				if( isCanceled() ){
		        	try{
		        		System.out.println("Deleting: " + savePath);
			        	Files.deleteIfExists(savePath);
			        } catch(IOException ex){
			        	throw new UncheckedIOException(ex);
			        }
					EventQueue.invokeLater(() -> {
						dialog.dispose();
					});
				} else{
	            	final Path outPath = convertImage(savePath, "jpg");
		            EventQueue.invokeLater(() -> {
                        addPage(outPath);
			            dialog.dispose();
		            });
				}
			}

			@Override
			public void onProgress(int pct){
				EventQueue.invokeLater(() -> {
					dialog.setValue(pct);
				});
			}

			@Override
			public void onCallback(){
				EventQueue.invokeLater(() -> {
					if( dialog.isCanceled() ){
						setCanceled(true);
					}
				});
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
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
    		return ScannerUtil.pickDevice();
    	}
    }

    private String makeTimeStamp(){
        LocalDateTime dt = LocalDateTime.now();
        return dt.format(timeStampFormatter);
    }
}