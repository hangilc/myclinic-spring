package jp.chang.myclinic.drawer.printer;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.BaseTSD.SIZE_T;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.platform.win32.WinUser.WindowProc;
import jp.chang.myclinic.drawer.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hangil on 2017/05/24.
 */
public class DrawerPrinter {

    public void print(Iterable<Op> ops){
        DialogResult dialogResult = printDialog(null, null);
        if( dialogResult.ok ){
            HDC hdc = createDC(dialogResult.devnamesData, dialogResult.devmodeData);
            if( hdc.getPointer() == Pointer.NULL ){
                throw new RuntimeException("createDC faield");
            }
            int jobId = beginPrint(hdc);
            if( jobId <= 0 ){
                throw new RuntimeException("StartDoc failed");
            }
            int dpix = getDpix(hdc);
            int dpiy = getDpiy(hdc);
            startPage(hdc);
            MyGdi32.INSTANCE.SetBkMode(hdc, PrinterConsts.TRANSPARENT);
            execOps(hdc, ops, dpix, dpiy);
            endPage(hdc);
            int endDocResult = endPrint(hdc);
            if( endDocResult <= 0 ){
                throw new RuntimeException("EndDoc failed");
            }
            deleteDC(hdc);
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

    private int getDpix(HDC hdc){
        return GDI32.INSTANCE.GetDeviceCaps(hdc, PrinterConsts.LOGPIXELSX);
    }

    private int getDpiy(HDC hdc){
        return GDI32.INSTANCE.GetDeviceCaps(hdc, PrinterConsts.LOGPIXELSY);
    }

    private int calcCoord(double mm, int dpi){
        return (int)(mmToInch(mm) * dpi);
    }

    private double mmToInch(double mm){
        return mm * 0.0393701;
    }

    private void moveTo(HDC hdc, int x, int y){
        boolean ok = MyGdi32.INSTANCE.MoveToEx(hdc, x, y, null);
        if( !ok ){
            throw new RuntimeException("MoveToEx failed");
        }
    }

    private void lineTo(HDC hdc, int x, int y){
        boolean ok = MyGdi32.INSTANCE.LineTo(hdc, x, y);
        if( !ok ){
            throw new RuntimeException("LineTo failed");
        }
    }

    private HFONT createFont(String fontName, int size, int weight, boolean italic){
        LOGFONT logfont = new LOGFONT();
        logfont.lfHeight = size;
        logfont.lfWeight = weight;
        logfont.lfItalic = italic ? 1 : 0;
        logfont.lfCharSet = PrinterConsts.DEFAULT_CHARSET;
        if( fontName.length() >= PrinterConsts.LF_FACESIZE ){
            throw new RuntimeException("Too long font name");
        }
        logfont.lfFaceName = fontName.toCharArray();
        return MyGdi32.INSTANCE.CreateFontIndirect(logfont);
    }

    private void deleteObject(HANDLE handle){
        boolean ok = GDI32.INSTANCE.DeleteObject(handle);
        if( !ok ){
            throw new RuntimeException("DeleteObject failed");
        }
    }

    private void selectObject(HDC hdc, HANDLE handle){
        GDI32.INSTANCE.SelectObject(hdc, handle);
    }

    private int RGB(int r, int g, int b){
        return r + (g << 8) + (b << 16);
    }

    private HPEN createPen(int penStyle, int width, int rgb){
        return MyGdi32.INSTANCE.CreatePen(penStyle, width, rgb);
    }

    private void execOps(HDC hdc, Iterable<Op> ops, int dpix, int dpiy){
        Map<String, HFONT> fontMap = new HashMap<>();
        Map<String, HPEN> penMap = new HashMap<>();
        for(Op op: ops){
            switch(op.getOpCode()){
                case MoveTo: {
                    OpMoveTo opMoveTo = (OpMoveTo)op;
                    int x = calcCoord(opMoveTo.getX(), dpix);
                    int y = calcCoord(opMoveTo.getY(), dpiy);
                    moveTo(hdc, x, y);
                    break;
                }
                case LineTo: {
                    OpLineTo opLineTo = (OpLineTo)op;
                    int x = calcCoord(opLineTo.getX(), dpix);
                    int y = calcCoord(opLineTo.getY(), dpiy);
                    lineTo(hdc, x, y);
                    break;
                }
                case CreateFont: {
                    OpCreateFont opCreateFont = (OpCreateFont)op;
                    int size = (int)(mmToInch(opCreateFont.getSize()) * dpiy);
                    HFONT font = createFont(opCreateFont.getFontName(), size, opCreateFont.getWeight(),
                            opCreateFont.isItalic());
                    fontMap.put(opCreateFont.getName(), font);
                    break;
                }
                case SetFont: {
                    OpSetFont opSetFont = (OpSetFont)op;
                    HFONT font = fontMap.get(opSetFont.getName());
                    selectObject(hdc, font);
                    break;
                }
                case DrawChars: {
                    OpDrawChars opDrawChars = (OpDrawChars)op;
                    List<Double> xs = opDrawChars.getXs();
                    List<Double> ys = opDrawChars.getYs();
                    char[] chars = opDrawChars.getChars().toCharArray();
                    for(int i=0;i<chars.length;i++){
                        double cx, cy;
                        if( i >= xs.size() ){
                            cx = xs.get(xs.size()-1);
                        } else {
                            cx = xs.get(i);
                        }
                        if( i >= ys.size() ){
                            cy = ys.get(ys.size()-1);
                        } else {
                            cy = ys.get(i);
                        }
                        int x = calcCoord(cx, dpix);
                        int y = calcCoord(cy, dpiy);
                        MyGdi32.INSTANCE.TextOut(hdc, x, y, new WString(String.valueOf(chars[i])), 1);
                    }
                    break;
                }
                case SetTextColor: {
                    OpSetTextColor opSetTextColor = (OpSetTextColor)op;
                    int rgb = RGB(opSetTextColor.getR(), opSetTextColor.getG(), opSetTextColor.getB());
                    MyGdi32.INSTANCE.SetTextColor(hdc, rgb);
                    break;
                }
                case CreatePen: {
                    OpCreatePen opCreatePen = (OpCreatePen)op;
                    int width = calcCoord(opCreatePen.getWidth(), dpix);
                    int rgb = RGB(opCreatePen.getR(), opCreatePen.getG(), opCreatePen.getB());
                    HPEN pen = createPen(PrinterConsts.PS_SOLID, width, rgb);
                    penMap.put(opCreatePen.getName(), pen);
                    break;
                }
                case SetPen: {
                    OpSetPen opSetPen = (OpSetPen)op;
                    HPEN pen = penMap.get(opSetPen.getName());
                    selectObject(hdc, pen);
                    break;
                }
                default: {
                    System.out.println("Unknown op: " + op);
                }
            }
        }
        for(HFONT font: fontMap.values()){
            deleteObject(font);
        }
        for(HPEN pen: penMap.values()){
            deleteObject(pen);
        }
    }

}
