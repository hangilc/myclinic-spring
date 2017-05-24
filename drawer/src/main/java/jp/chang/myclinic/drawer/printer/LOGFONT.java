package jp.chang.myclinic.drawer.printer;

import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.W32APITypeMapper;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import java.util.List;
import java.util.Arrays;

public class LOGFONT extends Structure implements WinDef, WinUser {
	
	public long lfHeight;
	public long lfWidth;
	public long lfEscapement;
	public long lfOrientation;
	public long lfWeight;
	public long lfItalic;
	public long lfUnderline;
	public long lfStrikeOut;
	public long lfCharSet;
	public long lfOutPrecision;
	public long lfClipPrecision;
	public long lfQuality;
	public long lfPitchAndFamily;
	public char[] lfFaceName = new char[PrinterConsts.LF_FACESIZE];

	@Override
	protected List<String> getFieldOrder(){
		return Arrays.asList(new String[] {
			"lfHeight",
			"lfWidth",
			"lfEscapement",
			"lfOrientation",
			"lfWeight",
			"lfItalic",
			"lfUnderline",
			"lfStrikeOut",
			"lfCharSet",
			"lfOutPrecision",
			"lfClipPrecision",
			"lfQuality",
			"lfPitchAndFamily",
			"lfFaceName"
		});
	}
}