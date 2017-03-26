package jp.chang.wia;

import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.WString;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.WTypes.BSTR;
import com.sun.jna.platform.win32.WTypes.VARTYPE;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.Guid.CLSID;

import java.util.List;
import java.util.Arrays;

public class PROPVARIANT extends Structure {

	public static class Value extends Union {
		public LONG lVal;
		public Pointer pointerValue;
		public BSTR bstrVal;
		public BSTRBLOB bstrblobValue;

		public Value(){}

		public Value(Pointer pointer){
			super(pointer);
		}

		@Override
		protected List<String> getFieldOrder(){
			return Arrays.asList(new String[]{
				"lVal", "pointerValue", "bstrVal", "bstrblobValue"
			});
		}
	}

	public VARTYPE vt;
	public WORD reserved1;
	public WORD reserved2;
	public WORD reserved3;
	public Value value;

	public PROPVARIANT(){}

	public PROPVARIANT(Pointer pointer){
		super(pointer);
	}

	public static PROPVARIANT createVariantCLSID(CLSID clsid){
		PROPVARIANT variant = new PROPVARIANT();
		variant.vt = new VARTYPE(Variant.VT_CLSID);
		variant.value.pointerValue = clsid.getPointer();
		return variant;
	}

	public static PROPVARIANT createVariantLONG(int value){
		PROPVARIANT variant = new PROPVARIANT();
		variant.vt = new VARTYPE(Variant.VT_I4);
		variant.value.lVal = new LONG(value);
		return variant;
	}

	@Override
	protected List<String> getFieldOrder(){
		return Arrays.asList(new String[]{
			"vt", "reserved1", "reserved2", "reserved3", "value"
		});
	}

	public int getVt(){
		return vt.intValue();
	}

	public Pointer getUnionPointer(){
		return (Pointer)value.readField("pointerValue");
	}

	public BSTR getUnionBSTR(){
		return (BSTR)value.readField("bstrVal");
	}

	public static short getVt(Memory memory, int offset){
		return memory.getShort(offset);
	}

	public static BSTR getBSTR(Memory memory, int offset){
		Pointer pointer = memory.getPointer(offset + 8);
		return new BSTR(pointer);
	}

}