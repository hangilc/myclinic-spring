package jp.chang.wia;

import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinDef.ULONG;
import com.sun.jna.platform.win32.WinDef.INT_PTR;

import java.util.List;
import java.util.Arrays;

public class BSTRBLOB extends Structure {
	public ULONG cbSize;
	public INT_PTR pData;

	@Override
	protected List<String> getFieldOrder(){
		return Arrays.asList(new String[]{
			"cbSize", "pData"
		});
	}
}