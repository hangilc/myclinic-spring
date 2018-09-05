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
import java.util.stream.Collectors;

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
            case "rollback":
                doRollback();
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
        out.println("Usage: REleaseCurrent list");
        out.println("Usage: REleaseCurrent rollback");
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

    private static String getCurrentVersion() throws IOException {
        return getVersion(getCurrentPath());
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
        Collections.reverse(result);
        return result;
    }

    private static void doList() throws IOException {
        String currentVersion = getCurrentVersion();
        for (Path path : listReleases()) {
            String release = path.getFileName().toString();
            if (release.equals(currentVersion)) {
                System.out.print("* ");
            }
            System.out.println(release);
        }
    }

    private static void doRollback() throws IOException, InterruptedException {
        String currentVersion = getCurrentVersion();
        List<String> releases = listReleases().stream().map(p -> p.getFileName().toString()).collect(Collectors.toList());
        Integer index = null;
        for (int i = 0; i < releases.size(); i++) {
            String release = releases.get(i);
            if (release.equals(currentVersion)) {
                index = i;
                break;
            }
        }
        if (index == null) {
            System.err.println("Cannot find current release.");
            System.exit(1);
        }
        index += 1;
        if (index < releases.size()) {
            String rollback = releases.get(index);
            Path newCurrent = getRepositoryPath().resolve(rollback);
            changeCurrent(newCurrent.toAbsolutePath().toString());
        } else {
            System.err.println("Cannot rollback.");
            System.exit(1);
        }
    }

    private static int changeCurrent(String release) throws IOException, InterruptedException {
        Path current = getCurrentPath();
        Path tmpCurrent = Paths.get(current.toAbsolutePath().toString() + "-tmp");
        int retCode = Runtime.getRuntime().exec(new String[]{
                "cmd.exe",
                "/c",
                "mklink",
                "/J",
                tmpCurrent.toAbsolutePath().toString(),
                release
        }).waitFor();
        if( retCode == 0 ){
            Files.deleteIfExists(current);
            Files.move(tmpCurrent, current);
        }
        return retCode;
    }

}
