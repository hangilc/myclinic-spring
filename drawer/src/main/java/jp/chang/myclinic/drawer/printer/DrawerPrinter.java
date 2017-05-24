package jp.chang.myclinic.drawer.printer;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.BaseTSD.SIZE_T;
import com.sun.jna.platform.win32.WinDef.ATOM;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.platform.win32.WinUser.WindowProc;
import jp.chang.myclinic.drawer.Op;

/**
 * Created by hangil on 2017/05/24.
 */
public class DrawerPrinter {

    public void print(Iterable<Op> ops){
        DialogResult dialogResult = printDialog(null, null);
        if( dialogResult.ok ){
            DevNamesInfo devNamesInfo = new DevNamesInfo(dialogResult.devnamesData);
            System.out.println(devNamesInfo);
            DevModeInfo devModeInfo = new DevModeInfo(dialogResult.devmodeData);
            System.out.println(devModeInfo);
            HDC hdc = createDC(dialogResult.devnamesData, dialogResult.devmodeData);
            if( hdc.getPointer() == Pointer.NULL ){
                throw new RuntimeException("createDC faield");
            }
            int jobId = beginPrint(hdc);
            if( jobId <= 0 ){
                throw new RuntimeException("StartDoc failed");
            }
            startPage(hdc);
            endPage(hdc);
            int endDocResult = endPrint(hdc);
            if( endDocResult <= 0 ){
                throw new RuntimeException("EndDoc failed");
            }
            boolean ok = deleteDC(hdc);
            System.out.println(ok);
        }
    }

    public static final int PD_ALLPAGES = 0x00000000;
    public static final int PD_SELECTION = 0x00000001;
    public static final int PD_PAGENUMS = 0x00000002;
    public static final int PD_NOSELECTION = 0x00000004;
    public static final int PD_NOPAGENUMS = 0x00000008;
    public static final int PD_USEDEVMODECOPIESANDCOLLATE = 0x00040000;
    public static final int PD_DISABLEPRINTTOFILE = 0x00080000;
    public static final int PD_HIDEPRINTTOFILE = 0x00100000;
    public static final int PD_CURRENTPAGE = 0x00400000;
    public static final int PD_NOCURRENTPAGE = 0x00800000;
    public static final int PD_RESULT_CANCEL = 0x0;
    public static final int PD_RESULT_PRINT = 0x1;
    public static final int PD_RESULT_APPLY = 0x2;
    public static final int START_PAGE_GENERAL = 0xFFFFFFFF;

    private static class NopWindowProc implements WinDef, WinUser.WindowProc {
        public LRESULT callback(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam){
            return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
        }
    }

    private static WindowProc winproc = new NopWindowProc();
    private static String windowClassName = "DRAWERWINDOW";
    static {
        WNDCLASSEX wndClass = new WNDCLASSEX();
        wndClass.lpfnWndProc = winproc;
        wndClass.hInstance = Kernel32.INSTANCE.GetModuleHandle(null);
        wndClass.lpszClassName = windowClassName;
        ATOM atom = User32.INSTANCE.RegisterClassEx(wndClass);
        if( atom.intValue() == 0 ){
            throw new RuntimeException("RegisterWindowEx failed");
        }
    }

    public static class DialogResult {
        public boolean ok;
        public byte[] devmodeData;
        public byte[] devnamesData;
        public int nCopies;

        DialogResult(){ }

        DialogResult(boolean ok, byte[] devmodeData, byte[] devnamesData, int nCopies){
            this.ok = ok;
            this.devmodeData = devmodeData;
            this.devnamesData = devnamesData;
            this.nCopies = nCopies;
        }
    }

    public DialogResult printDialog(byte[] devmodeBase, byte[] devnamesBase){
        PRINTDLGEX pd = new PRINTDLGEX();
        WinDef.HWND hwnd = createWindow();
        if( hwnd == null ){
            throw new RuntimeException("Printer.createWindow failed");
        }
        pd.hwndOwner = hwnd;
        pd.Flags.setValue(PD_NOPAGENUMS);
        pd.nCopies.setValue(1);
        pd.nStartPage.setValue(START_PAGE_GENERAL);
        if( devmodeBase != null ){
            pd.hDevMode = allocHandle(devmodeBase);
        }
        if( devnamesBase != null ){
            pd.hDevNames = allocHandle(devnamesBase);
        }
        HRESULT res = Comdlg32.INSTANCE.PrintDlgEx(pd);
        DialogResult result;
        if( res.intValue() == 0 && pd.dwResultAction.intValue() == 1 ){
            Pointer pDevMode = MyKernel32.INSTANCE.GlobalLock(pd.hDevMode);
            DEVMODE devmode = new DEVMODE(pDevMode);
            byte[] devmodeData = copyDevModeData(pDevMode);
            MyKernel32.INSTANCE.GlobalUnlock(pd.hDevMode);
            MyKernel32.INSTANCE.GlobalFree(pd.hDevMode);
            Pointer pDevNames = MyKernel32.INSTANCE.GlobalLock(pd.hDevNames);
            byte[] devnamesData = copyDevNamesData(pDevNames);
            MyKernel32.INSTANCE.GlobalUnlock(pd.hDevNames);
            MyKernel32.INSTANCE.GlobalFree(pd.hDevNames);
            result = new DialogResult(true, devmodeData, devnamesData, pd.nCopies.intValue());

        } else {
            result = new DialogResult();
        }
        System.out.println(disposeWindow(hwnd));
        return result;
    }

    private HWND createWindow(){
        HMODULE hInst = Kernel32.INSTANCE.GetModuleHandle(null);
        return User32.INSTANCE.CreateWindowEx(WinUser.WS_OVERLAPPED, windowClassName, "Dummy Window",
                0, 0, 0, 0, 0, null, null, hInst, null);
    }

    private HANDLE allocHandle(byte[] data){
        UINT flag = new UINT(MyKernel32.GMEM_MOVEABLE);
        //flag.setValue(MyKernel32.GMEM_MOVEABLE);
        BaseTSD.SIZE_T size = new SIZE_T(data.length);
        HANDLE handle = MyKernel32.INSTANCE.GlobalAlloc(flag, size);
        Pointer ptr = MyKernel32.INSTANCE.GlobalLock(handle);
        ptr.write(0, data, 0, data.length);
        MyKernel32.INSTANCE.GlobalUnlock(handle);
        return handle;
    }

    private boolean disposeWindow(HWND hwnd){
        return User32.INSTANCE.DestroyWindow(hwnd);
    }

    private byte[] copyDevNamesData(Pointer pDevNames){
        DEVNAMES devnames = new DEVNAMES(pDevNames);
        String outputName = pDevNames.getWideString(devnames.wOutputOffset.intValue()*2);
        int devnamesSize = (devnames.wOutputOffset.intValue() + outputName.length() + 1)*2;
        return pDevNames.getByteArray(0, devnamesSize);
    }

    private byte[] copyDevModeData(Pointer pDevMode){
        DEVMODE devmode = new DEVMODE(pDevMode);
        int devmodeSize = devmode.dmSize.intValue() + devmode.dmDriverExtra.intValue();
        return pDevMode.getByteArray(0, devmodeSize);
    }

    private HDC createDC(byte[] devnamesData, byte[] devmodeData){
        DevNamesInfo devNamesInfo = new DevNamesInfo(devnamesData);
        Pointer devmodePointer = new Memory(devmodeData.length);
        devmodePointer.write(0, devmodeData, 0, devmodeData.length);
        return MyGdi32.INSTANCE.CreateDC(null, new WString(devNamesInfo.getDevice()), null, devmodePointer);
    }

    private boolean deleteDC(HDC hdc){
        return MyGdi32.INSTANCE.DeleteDC(hdc);
    }

    private int beginPrint(HDC hdc){
        DOCINFO docinfo = new DOCINFO();
        docinfo.cbSize = docinfo.size();
        docinfo.docName = new WString("drawer printing");
        int ret = MyGdi32.INSTANCE.StartDoc(hdc, docinfo);
        return ret;
    }

    private int endPrint(HDC hdc){
        return MyGdi32.INSTANCE.EndDoc(hdc);
    }

    private int abortPrint(HDC hdc){
        return MyGdi32.INSTANCE.AbortDoc(hdc);
    }

    private void startPage(HDC hdc){
        int ret = MyGdi32.INSTANCE.StartPage(hdc);
        if( ret <= 0 ){
            throw new RuntimeException("StartPage failed");
        }
    }

    private void endPage(HDC hdc){
        int ret = MyGdi32.INSTANCE.EndPage(hdc);
        if( ret <= 0 ){
            throw new RuntimeException("EndPage failed");
        }
    }

}
