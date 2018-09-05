package jp.chang.myclinic.management;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseCurrent {

    //private static Logger logger = LoggerFactory.getLogger(ReleaseCurrent.class);
    private static Pattern releaseDirPattern = Pattern.compile("myclinic-release-\\d+-\\d+");
    private static Pattern releaseStampFilePattern = Pattern.compile("(myclinic-release-\\d+-\\d+)\\.txt");

    public static void main(String[] args) throws Exception {
        String command = "show";
        if (args.length > 0) {
            command = args[0];
        }
        switch (command) {
            case "show":
                doShow();
                break;
            case "list":
                doList();
                break;
            default:
                System.err.println("Unknown command: " + command);
                doUsage(System.err);
                break;
        }
    }

    private static void doUsage(PrintStream out) {
        out.println("Usage: ReleaseCurrent ");
        out.println("Usage: REleaseCurrent show");
    }

    private static void doShow() throws IOException {
        Path current = getCurrentPath();
        String ver = getVersion(current);
        System.out.println(ver);
    }

    private static Path getRepositoryPath() {
        return Paths.get(System.getenv("MYCLINIC_REPOSITORY"));
    }

    private static Path getCurrentPath() {
        return getRepositoryPath().resolve("current");
    }

    private static String getVersion(Path releaseDirPath) throws IOException {
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

    private static List<Path> listReleases() throws IOException {
        List<Path> result = new ArrayList<>();
        for (Path path : Files.newDirectoryStream(getRepositoryPath())) {
            Matcher matcher = releaseDirPattern.matcher(path.getFileName().toString());
            if (matcher.matches()) {
                result.add(path);
            }
        }
        Collections.sort(result);
        return result;
    }

    private static void doList() throws IOException {
        for(Path path: listReleases()){
            System.out.println(path.getFileName().toString());
        }
    }

}
