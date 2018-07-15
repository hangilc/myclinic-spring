import jp.chang.myclinic.masterlib.MasterDownloader
import java.nio.file.Paths
import java.nio.file.Files

val d = MasterDownloader()
val saveDir = Paths.get("./master-files")
if( Files.notExists(saveDir) ){
	Files.createDirectory(saveDir)
}
d.downloadShinryou(saveDir.resolve(MasterDownloader.DEFAULT_SHINRYOU_FILENAME))
d.downloadIyakuhin(saveDir.resolve(MasterDownloader.DEFAULT_IYAKUHIN_FILENAME))
d.downloadKizai(saveDir.resolve(MasterDownloader.DEFAULT_KIZAI_FILENAME))
d.downloadShoubyoumei(saveDir.resolve(MasterDownloader.DEFAULT_SHOUBYOUMEI_FILENAME))
d.downloadShuushokugo(saveDir.resolve(MasterDownloader.DEFAULT_SHUUSHOKUGO_FILENAME))


