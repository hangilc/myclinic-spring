package jp.chang.wia;

import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.UnknownVTable;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.platform.win32.Guid.IID;
import com.sun.jna.platform.win32.Guid.REFIID;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class WiaDataCallbackImpl extends Structure {

	public WiaDataCallbackImpl(){}

	public WiaDataCallbackImpl(Pointer pointer){
		super(pointer);
	}

	public static WiaDataCallback create(BandedDataCallbackCallback callback){
		Vtbl lpVtbl = new Vtbl();
		lpVtbl.QueryInterfaceCallback = new UnknownVTable.QueryInterfaceCallback(){
			@Override
			public HRESULT invoke(Pointer thisPointer, REFIID refid, PointerByReference ppvObject) {
				if( ppvObject == null ){
					return new HRESULT(WinError.E_POINTER);
				}
				IID iid = refid.getValue();
				if( iid.equals(IUnknown.IID_IUNKNOWN) || iid.equals(IWiaDataCallback.IID_IWiaDataCallback) ){
					ppvObject.setValue(thisPointer);
					new WiaDataCallbackImpl(thisPointer).refCount += 1;
					return WinError.S_OK;
				} else {
					return new HRESULT(WinError.E_NOINTERFACE);
				}
			}
		};
		lpVtbl.AddRefCallback = new UnknownVTable.AddRefCallback(){
			@Override
			public int invoke(Pointer thisPointer){
				WiaDataCallbackImpl self = new WiaDataCallbackImpl(thisPointer);
				self.read();
				System.out.printf("adding reference: %d\n", self.refCount);
				self.refCount += 1;
				self.write();
				return self.refCount;
			}
		};
		lpVtbl.ReleaseCallback = new UnknownVTable.ReleaseCallback(){
			@Override
			public int invoke(Pointer thisPointer){
				WiaDataCallbackImpl self = new WiaDataCallbackImpl(thisPointer);
				self.read();
				self.refCount -= 1;
				if( self.refCount == 0 ){
					allocated.remove(thisPointer);
					System.out.printf("allocated: %d\n", allocated.size());
				}
				self.write();
				return self.refCount;
			}
		};
		lpVtbl.BandedDataCallbackCallback = callback;
		lpVtbl.write();
		WiaDataCallbackImpl wiaDataCallbackImpl = new WiaDataCallbackImpl();
		wiaDataCallbackImpl.lpVtbl = new Vtbl.ByReference(lpVtbl.getPointer());
		wiaDataCallbackImpl.lpVtbl.read();
		wiaDataCallbackImpl.refCount = 1;
		wiaDataCallbackImpl.write();
		allocated.add(wiaDataCallbackImpl.getPointer());
		return new WiaDataCallback(wiaDataCallbackImpl.getPointer());
	}

	public static interface BandedDataCallbackCallback extends StdCallCallback {
		HRESULT invoke(Pointer thisPointer, LONG lMessage, LONG lStatus, LONG lPercentComplete,
			LONG lOffset, LONG lLength, LONG lReserved, LONG lResLength, PointerByReference pbBuffer);
	}

	public static class Vtbl extends Structure {

		public static class ByReference extends Vtbl implements Structure.ByReference {
			public ByReference(){}

			public ByReference(Pointer pointer){
				super(pointer);
			}
		}

		public Vtbl(){}

		public Vtbl(Pointer pointer){
			super(pointer);
		}

		public UnknownVTable.QueryInterfaceCallback QueryInterfaceCallback;
		public UnknownVTable.AddRefCallback AddRefCallback;
		public UnknownVTable.ReleaseCallback ReleaseCallback;
		public BandedDataCallbackCallback BandedDataCallbackCallback;

		@Override
		protected List<String> getFieldOrder(){
			return Arrays.asList(new String[]{
				"QueryInterfaceCallback", "AddRefCallback", "ReleaseCallback", "BandedDataCallbackCallback"
			});
		}
	}

	private static Set<Pointer> allocated = new HashSet<Pointer>();

	public Vtbl.ByReference lpVtbl;
	public int refCount;

	@Override
	protected List<String> getFieldOrder(){
		return Arrays.asList(new String[]{
			"lpVtbl", "refCount"
		});
	}
}

