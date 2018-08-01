package jp.chang.myclinic.winutil.main;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Guid.REFIID;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;
import jp.chang.myclinic.winutil.*;

public class CreateShortcut {

    public static void main( String[] args )
    {
        ComUtil.CoInitialize();
        PointerByReference pp = new PointerByReference();
        HRESULT hr = Ole32.INSTANCE.CoCreateInstance(ComUtil.CLSID_ShellLink, null,
                WTypes.CLSCTX_INPROC_SERVER, IShellLink.IID_IShellLink, pp);
        COMUtils.checkRC(hr);
        ShellLink shellLink = new ShellLink(pp.getValue());
        String target = "C:/Program Files/erl8.3/bin/werl.exe";
        Pointer pTarget = new Memory((target.length() + 1) * 2);
        pTarget.setWideString(0, target);
        hr = shellLink.SetPath(pTarget);
        COMUtils.checkRC(hr);
        Pointer path = new Memory((WinDef.MAX_PATH + 1)*2);
        hr = shellLink.GetPath(path, WinDef.MAX_PATH+1, new Pointer(0), 0);
        COMUtils.checkRC(hr);
        System.out.println(path.getWideString(0));
        System.out.println("hello, world");
        {
            String argStr = "--help";
            Pointer argStrPointer = new Memory((argStr.length()+1)*2);
            argStrPointer.setWideString(0, argStr);
            hr = shellLink.SetArguments(argStrPointer);
            COMUtils.checkRC(hr);
        }
        {
            String str = "C:/Users/hangil/work/learn-java/myclinic-spring";
            Pointer p = new Memory((str.length()+1)*2);
            p.setWideString(0, str);
            hr = shellLink.SetWorkingDirectory(p);
            COMUtils.checkRC(hr);
        }
        PointerByReference persistFilePointer = new PointerByReference();
        hr = shellLink.QueryInterface(new REFIID(IPersistFile.IID_IPersistFile), persistFilePointer);
        COMUtils.checkRC(hr);
        PersistFile persistFile = new PersistFile(persistFilePointer.getValue());
        String savePath = "C:/Users/hangil/shortcut.lnk";
        Pointer saveFile = Ole32.INSTANCE.CoTaskMemAlloc((savePath.length()+1)*2);
        WTypes.LPOLESTR olestr = new WTypes.LPOLESTR(saveFile);
        olestr.setValue(savePath);
        hr = persistFile.Save(olestr, new WinDef.BOOL(true));
        COMUtils.checkRC(hr);
        Ole32.INSTANCE.CoTaskMemFree(saveFile);
        persistFile.Release();
        shellLink.Release();
    }

}

