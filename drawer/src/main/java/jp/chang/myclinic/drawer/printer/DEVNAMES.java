package jp.chang.myclinic.drawer.printer;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.W32APITypeMapper;

import java.util.List;
import java.util.Arrays;

public class DEVNAMES extends Structure implements WinDef, WinUser {
	public WORD wDriverOffset;
	public WORD wDeviceOffset;
	public WORD wOutputOffset;
	public WORD wDefault;

	public DEVNAMES(Pointer mem){
		super(mem);
		read();
	}

	@Override
	protected List<String> getFieldOrder(){
		return Arrays.asList(new String[] {
			"wDriverOffset", "wDeviceOffset", "wOutputOffset", "wDefault"
		});
	}
}