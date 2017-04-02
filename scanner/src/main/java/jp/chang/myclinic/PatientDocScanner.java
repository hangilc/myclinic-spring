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

	private void doStart(ActionEvent event){
		numPages += 1;
		numPagesLabel.setText(String.valueOf(numPages));
		pack();
	}

	private String makeTimeStamp(){
		LocalDateTime dt = LocalDateTime.now();
		return dt.format(timeStampFormatter);
	}

}