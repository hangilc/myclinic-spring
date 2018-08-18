package jp.chang.myclinic.scanner;

import jp.chang.wia.Wia;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
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

    static Path convertImage(Path source, String format) throws IOException {
        String srcFileName = source.getFileName().toString();
        String dstFileName = srcFileName.replaceFirst("\\.bmp$", "." + format);
        Path output = source.resolveSibling(dstFileName);
        BufferedImage src = ImageIO.read(source.toFile());
        boolean ok = ImageIO.write(src, format, output.toFile());
        if( !ok ){
            throw new RuntimeException("image conversion failed");
        }
        return output;
    }


}
