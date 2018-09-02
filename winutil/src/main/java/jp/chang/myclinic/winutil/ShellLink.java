package jp.chang.myclinic.winutil;

import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT.HRESULT;
import com.sun.jna.ptr.PointerByReference;

import static jp.chang.myclinic.winutil.WinConsts.STGM_READWRITE;

public class ShellLink {

    //private static Logger logger = LoggerFactory.getLogger(ShellLink.class);
    private ShellLinkApi api;

    public ShellLink() {
        PointerByReference pp = new PointerByReference();
        HRESULT hr = Ole32.INSTANCE.CoCreateInstance(ComUtil.CLSID_ShellLink, null,
                WTypes.CLSCTX_INPROC_SERVER, IShellLink.IID_IShellLink, pp);
        COMUtils.checkRC(hr);
        api = new ShellLinkApi(pp.getValue());
    }

    public void close(){
        api.Release();
    }

    public void setPath(String target){
        ApiString targetStr = new ApiString(target);
        HRESULT hr = api.SetPath(targetStr.asPointer());
        COMUtils.checkRC(hr);
    }

    public String getPath(){
        ApiString apiStr = new ApiString(WinDef.MAX_PATH+1);
        HRESULT hr = api.GetPath(apiStr.asPointer(), WinDef.MAX_PATH+1, null, 0);
        COMUtils.checkRC(hr);
        return apiStr.toString();
    }

    public void setArguments(String arguments){
        ApiString apiArgStr = new ApiString(arguments);
        HRESULT hr = api.SetArguments(apiArgStr.asPointer());
        COMUtils.checkRC(hr);
    }

    public void setWorkingDirectory(String dir){
        ApiString apiDirStr = new ApiString(dir);
        HRESULT hr = api.SetWorkingDirectory(apiDirStr.asPointer());
        COMUtils.checkRC(hr);
    }

    public void save(String savePath){
        PointerByReference pp = new PointerByReference();
        HRESULT hr = api.QueryInterface(new Guid.REFIID(IPersistFile.IID_IPersistFile), pp);
        COMUtils.checkRC(hr);
        PersistFile persistFile = new PersistFile(pp.getValue());
        OleString savePathOle = new OleString(savePath);
        hr = persistFile.Save(savePathOle.asOleStr(), new WinDef.BOOL(true));
        COMUtils.checkRC(hr);
        persistFile.Release();
        savePathOle.close();
    }

    public void load(String path){
        PointerByReference pp = new PointerByReference();
        HRESULT hr = api.QueryInterface(new Guid.REFIID(IPersistFile.IID_IPersistFile), pp);
        COMUtils.checkRC(hr);
        PersistFile persistFile = new PersistFile(pp.getValue());
        OleString loadPathOle = new OleString(path);
        hr = persistFile.Load(loadPathOle.asOleStr(), new WinDef.DWORD(STGM_READWRITE));
        COMUtils.checkRC(hr);
        persistFile.Release();
        loadPathOle.close();
    }

}
