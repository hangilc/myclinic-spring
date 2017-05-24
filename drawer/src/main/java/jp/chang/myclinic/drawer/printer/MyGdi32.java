package jp.chang.myclinic.drawer.printer;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinGDI.ICONINFO;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinNT;

public interface MyGdi32 extends StdCallLibrary, WinUser, WinNT {
    MyGdi32 INSTANCE = (MyGdi32)Native.loadLibrary("gdi32", MyGdi32.class, W32APIOptions.DEFAULT_OPTIONS);

    boolean GetTextExtentPoint32(HDC hdc, WString text, int c, SIZE size);
    HFONT CreateFontIndirect(LOGFONT logfont);
    HDC CreateDC(WString driver, WString device, WString output, Pointer devmode);
    boolean DeleteDC(HDC hdc);
}
