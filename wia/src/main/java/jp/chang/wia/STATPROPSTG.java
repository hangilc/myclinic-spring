package jp.chang.wia;

import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WTypes.VARTYPE;

import java.util.List;
import java.util.Arrays;

public class STATPROPSTG extends Structure {

	public WString lpwstrName;
	public WiaTypes.PROPID propid;
	public VARTYPE vt;

	@Override
	protected List<String> getFieldOrder(){
		return Arrays.asList(new String[]{
			"lpwstrName", "propid", "vt"
		});
	}
}
