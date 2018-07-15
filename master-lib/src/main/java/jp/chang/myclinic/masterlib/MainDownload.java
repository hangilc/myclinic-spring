package jp.chang.myclinic.masterlib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainDownload {

    public static void main(String[] args) throws IOException {
        MasterDownloader downloader = new MasterDownloader();
        Path saveDir = Paths.get("./master-files");
        if(Files.notExists(saveDir) ){
            Files.createDirectory(saveDir);
        }
        downloader.downloadShinryou(saveDir.resolve(MasterDownloader.DEFAULT_SHINRYOU_FILENAME));
        downloader.downloadShinryou(saveDir.resolve(MasterDownloader.DEFAULT_SHINRYOU_FILENAME));
        downloader.downloadIyakuhin(saveDir.resolve(MasterDownloader.DEFAULT_IYAKUHIN_FILENAME));
        downloader.downloadKizai(saveDir.resolve(MasterDownloader.DEFAULT_KIZAI_FILENAME));
        downloader.downloadShoubyoumei(saveDir.resolve(MasterDownloader.DEFAULT_SHOUBYOUMEI_FILENAME));
        downloader.downloadShuushokugo(saveDir.resolve(MasterDownloader.DEFAULT_SHUUSHOKUGO_FILENAME));
    }

}
