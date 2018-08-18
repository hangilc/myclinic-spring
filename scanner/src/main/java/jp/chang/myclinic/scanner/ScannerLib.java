package jp.chang.myclinic.scanner;

import jp.chang.wia.Wia;

import java.util.List;
import java.util.function.Consumer;

class ScannerLib {

    //private static Logger logger = LoggerFactory.getLogger(ScannerLib.class);

    private ScannerLib() {

    }

    static String getSacnnerDeviceSetting(){
        String deviceId = ScannerSetting.INSTANCE.defaultDevice;
        if( !"".equals(deviceId) ){
            return deviceId;
        } else {
            return null;
        }
    }

    static String chooseScannerDevice(Consumer<String> alertFunc){
        List<Wia.Device> devices = Wia.listDevices();
        if (devices.size() == 0) {
            alertFunc.accept("接続された。スキャナーがみつかりません。");
            return null;
        } else if (devices.size() == 1) {
            return devices.get(0).deviceId;
        } else {
            return Wia.pickScannerDevice();
        }
    }

    static int getScannerResolutionSetting(){
        return ScannerSetting.INSTANCE.dpi;
    }

}
