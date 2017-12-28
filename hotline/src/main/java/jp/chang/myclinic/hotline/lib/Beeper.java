package jp.chang.myclinic.hotline.lib;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;


public interface Beeper extends StdCallLibrary {
    Beeper INSTANCE = (Beeper) Native.loadLibrary("User32", Beeper.class,
            W32APIOptions.DEFAULT_OPTIONS);

    WinDef.BOOL MessageBeep(int uType);
}

//public interface MyKernel32 extends StdCallLibrary {
//    MyKernel32 INSTANCE = (MyKernel32) Native.loadLibrary("Kernel32", MyKernel32.class,
//            W32APIOptions.DEFAULT_OPTIONS);
//
//    HANDLE GlobalAlloc(int uFlag, SIZE_T dwBytes);
//}
