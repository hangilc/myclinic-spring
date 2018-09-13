package jp.chang.myclinic.management.lib;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseLib {

    //private static Logger logger = LoggerFactory.getLogger(ReleaseLib.class);
    private static Pattern releaseDirPattern = Pattern.compile("myclinic-release-\\d+-\\d+");
    private static Pattern releaseStampFilePattern = Pattern.compile("(myclinic-release-\\d+-\\d+)\\.txt");

    private ReleaseLib() {

    }

    public static Path getRepositoryPath() {
        return Paths.get(System.getenv("MYCLINIC_REPOSITORY"));
    }

    public static Path getCurrentPath() {
        return getRepositoryPath().resolve("current");
    }

    public static String getVersion(Path releaseDirPath) throws IOException {
        DirectoryStream<Path> paths = Files.newDirectoryStream(releaseDirPath);
        for (Path path : paths) {
            Path fileName = path.getFileName();
            Matcher matcher = releaseStampFilePattern.matcher(fileName.toString());
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    public static String getLabel(Path releaseDirPath) throws IOException {
        Path labelFile = releaseDirPath.resolve("label.txt");
        if( Files.exists(labelFile) ){
            return String.join("", Files.readAllLines(labelFile)).trim();
        } else {
            return "";
        }
    }

    public static void createLabel(Path releaseDirPath, String label) throws IOException {
        if( label == null ){
            label = "";
        }
        Files.write(releaseDirPath.resolve("label.txt"), label.getBytes(StandardCharsets.UTF_8));
    }

    public static String getCurrentVersion() throws IOException {
        Path curr = getCurrentPath();
        if( Files.exists(curr) ) {
            return getVersion(getCurrentPath());
        } else {
            return null;
        }
    }

    public static List<Path> listReleases() throws IOException {
        List<Path> result = new ArrayList<>();
        for (Path path : Files.newDirectoryStream(getRepositoryPath())) {
            Matcher matcher = releaseDirPattern.matcher(path.getFileName().toString());
            if (matcher.matches()) {
                result.add(path);
            }
        }
        Collections.sort(result);
        Collections.reverse(result);
        return result;
    }


}
