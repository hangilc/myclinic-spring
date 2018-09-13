package jp.chang.myclinic.management;

import jp.chang.myclinic.management.lib.ReleaseLib;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ReleaseCurrent {

    //private static Logger logger = LoggerFactory.getLogger(ReleaseCurrent.class);

    public static void main(String[] args) throws Exception {
        String command = "show";
        if (args.length > 0) {
            command = args[0];
        }
        switch (command) {
            case "show":
                doShow();
                break;
            case "open":
                doOpen();
                break;
            case "list":
                doList();
                break;
            case "rollback":
                doRollback();
                break;
            case "help":
                doUsage(System.out);
                break;
            default:
                System.err.println("Unknown command: " + command);
                doUsage(System.err);
                break;
        }
    }

    private static void doUsage(PrintStream out) {
        out.println("Usage: REleaseCurrent [show]");
        out.println("Usage: REleaseCurrent open");
        out.println("Usage: REleaseCurrent list");
        out.println("Usage: REleaseCurrent rollback");
        out.println("Usage: REleaseCurrent help");
    }

    private static void doShow() throws IOException {
        Path current = ReleaseLib.getCurrentPath();
        if( Files.exists(current) ){
            String ver = ReleaseLib.getCurrentVersion();
            String label = ReleaseLib.getLabel(current);
            System.out.println(ver + " " + label);
        }
    }

    private static void doOpen() throws IOException {
        Path current = ReleaseLib.getCurrentPath();
        if( Files.exists(current) ) {
            Runtime.getRuntime().exec(new String[]{
                    "cmd.exe",
                    "/c",
                    "start",
                    current.toAbsolutePath().toString()
            });
        }
    }

    private static void doList() throws IOException {
        String currentVersion = ReleaseLib.getCurrentVersion();
        for (Path path : ReleaseLib.listReleases()) {
            String release = path.getFileName().toString();
            String label = ReleaseLib.getLabel(path);
            if (release.equals(currentVersion)) {
                System.out.print("* ");
            } else {
                System.out.print("  ");
            }
            System.out.println(release + " " + label);
        }
    }

    private static void doRollback() throws IOException, InterruptedException {
        String currentVersion = ReleaseLib.getCurrentVersion();
        List<String> releases = ReleaseLib.listReleases().stream()
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
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
            Path newCurrent = ReleaseLib.getRepositoryPath().resolve(rollback);
            changeCurrent(newCurrent.toAbsolutePath().toString());
        } else {
            System.err.println("Cannot rollback.");
            System.exit(1);
        }
    }

    private static int changeCurrent(String release) throws IOException, InterruptedException {
        Path current = ReleaseLib.getCurrentPath();
        Path tmpCurrent = Paths.get(current.toAbsolutePath().toString() + "-tmp");
        int retCode = Runtime.getRuntime().exec(new String[]{
                "cmd.exe",
                "/c",
                "mklink",
                "/J",
                tmpCurrent.toAbsolutePath().toString(),
                release
        }).waitFor();
        if (retCode == 0) {
            Files.deleteIfExists(current);
            Files.move(tmpCurrent, current);
        }
        return retCode;
    }

}
