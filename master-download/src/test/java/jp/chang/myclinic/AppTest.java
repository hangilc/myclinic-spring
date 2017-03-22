package jp.chang.myclinic.master;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Optional;

public class AppTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testIyakuhinDownload() throws IOException {
        MasterDownloader downloader = new MasterDownloader();
        Path path = Paths.get(tempFolder.getRoot().getAbsolutePath(), MasterDownloader.DEFAULT_IYAKUHIN_FILENAME);
        downloader.downloadIyakuhin(path);
        assertTrue( Files.exists(path) );
        FileSystem fs = FileSystems.newFileSystem(path, null);
        Path zipFilePath = fs.getPath("y.csv");
        assertTrue( Files.exists(zipFilePath) );
    }

    @Test
    public void testShinryouDownload() throws IOException {
        MasterDownloader downloader = new MasterDownloader();
        Path path = Paths.get(tempFolder.getRoot().getAbsolutePath(), MasterDownloader.DEFAULT_SHINRYOU_FILENAME);
        downloader.downloadShinryou(path);
        assertTrue( Files.exists(path) );
        FileSystem fs = FileSystems.newFileSystem(path, null);
        Path zipFilePath = fs.getPath("s.csv");
        assertTrue( Files.exists(zipFilePath) );
    }

    @Test
    public void testKizaiDownload() throws IOException {
        MasterDownloader downloader = new MasterDownloader();
        Path path = Paths.get(tempFolder.getRoot().getAbsolutePath(), MasterDownloader.DEFAULT_KIZAI_FILENAME);
        downloader.downloadKizai(path);
        assertTrue( Files.exists(path) );
        FileSystem fs = FileSystems.newFileSystem(path, null);
        Path zipFilePath = fs.getPath("t.csv");
        assertTrue( Files.exists(zipFilePath) );
    }

    @Test
    public void testShoubyoumeiDownload() throws IOException {
        MasterDownloader downloader = new MasterDownloader();
        Path path = Paths.get(tempFolder.getRoot().getAbsolutePath(), MasterDownloader.DEFAULT_SHOUBYOUMEI_FILENAME);
        downloader.downloadShoubyoumei(path);
        assertTrue( Files.exists(path) );
        FileSystem fs = FileSystems.newFileSystem(path, null);
        boolean found = false;
        PathMatcher matcher = fs.getPathMatcher("glob:**/b_*.txt");
        for(Path rootDir: fs.getRootDirectories()){
            Optional<Path> p = Files.list(rootDir)
                .filter(pp -> {
                    //System.out.println(pp);
                    return matcher.matches(pp);
                })
                .findFirst();
            if( p.isPresent() ){
                found = true;
                break;
            }
        }
        assertTrue( found );
    }

    @Test
    public void testShuushokugoDownload() throws IOException {
        MasterDownloader downloader = new MasterDownloader();
        Path path = Paths.get(tempFolder.getRoot().getAbsolutePath(), MasterDownloader.DEFAULT_SHUUSHOKUGO_FILENAME);
        downloader.downloadShuushokugo(path);
        assertTrue( Files.exists(path) );
        FileSystem fs = FileSystems.newFileSystem(path, null);
        boolean found = false;
        PathMatcher matcher = fs.getPathMatcher("glob:**/z_*.txt");
        for(Path rootDir: fs.getRootDirectories()){
            Optional<Path> p = Files.list(rootDir)
                .filter(pp -> {
                    //System.out.println(pp);
                    return matcher.matches(pp);
                })
                .findFirst();
            if( p.isPresent() ){
                found = true;
                break;
            }
        }
        assertTrue( found );
    }

}
