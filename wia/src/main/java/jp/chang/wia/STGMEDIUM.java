package jp.chang.wia;

import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import java.util.List;
import java.util.Arrays;

public class STGMEDIUM extends Structure {
	public static class UnionValue extends Union {
		public HANDLE handle;
		public Pointer pointer;

		@Override
		protected List<String> getFieldOrder(){
			return Arrays.asList(new String[]{
				"handle", "pointer"
			});
		}
	}

	public DWORD tymed;
	public UnionValue unionValue;
	public Pointer unkForRelease;

	@Override
	protected List<String> getFieldOrder(){
		return Arrays.asList(new String[]{
			"tymed", "unionValue", "unkForRelease"
		});
	}
}