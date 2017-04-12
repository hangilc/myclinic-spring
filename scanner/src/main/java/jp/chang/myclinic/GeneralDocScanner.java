package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import jp.chang.wia.Wia;
import jp.chang.wia.WiaConsts;
import jp.chang.wia.WiaDevMgr;

import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WTypes.BSTR;

public class GeneralDocScanner extends JDialog {

	public GeneralDocScanner(Frame owner){
		super(owner, "一般書類のスキャン", true);
		add(makeCommandPanel(), BorderLayout.SOUTH);
		pack();
	}

	private Component makeCommandPanel(){
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

	private void doStart(ActionEvent event){
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		{
			Path savePath = ScannerSetting.INSTANCE.savingDir;
			if( savePath != null ){
				jfc.setCurrentDirectory(savePath.toFile());
			}
		}
		int retVal = jfc.showOpenDialog(this);
		if( retVal == JFileChooser.APPROVE_OPTION ){
			File file = jfc.getSelectedFile();
			Path path = file.toPath();
			String fileName = path.getFileName().toString();
            WiaDevMgr devMgr = Wia.createWiaDevMgr();
            HWND hwnd = Kernel32.INSTANCE.GetConsoleWindow();
            Guid.IID iidFormat = new Guid.IID(Guid.IID_NULL);
            HRESULT hr = devMgr.GetImageDlg(hwnd, 
                new LONG(WiaConsts.StiDeviceTypeScanner),
                new LONG(0),
                new LONG(WiaConsts.WIA_INTENT_NONE),
                null,
                new BSTR(path.toString()),
                iidFormat
            );
            COMUtils.checkRC(hr);
            devMgr.Release();
            System.out.println("image saved as: " + path);
			System.out.println(fileName);
		}
	}

}