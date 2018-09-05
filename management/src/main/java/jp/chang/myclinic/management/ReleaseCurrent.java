package jp.chang.myclinic.management;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseCurrent {

    //private static Logger logger = LoggerFactory.getLogger(ReleaseCurrent.class);

    public static void main(String[] args) throws Exception {
        String command = "show";
        if( args.length > 0 ){
            command = args[0];
        }
        switch(command){
            case "show":
                doShow();
                break;
            default:
                System.err.println("Unknown command: " + command);
                doUsage(System.err);
                break;
        }
    }

    private static void doUsage(PrintStream out){
        out.println("Usage: ReleaseCurrent ");
        out.println("Usage: REleaseCurrent show");
    }

    private static void doShow() throws IOException {
        Path current = getCurrentPath();
        String ver = getVersion(current);
        System.out.println(ver);
    }

    private static Path getCurrentPath(){
        Path repo = Paths.get(System.getenv("MYCLINIC_REPOSITORY"));
        return repo.resolve("current");
    }

    private static String getVersion(Path releaseDirPath) throws IOException {
        Pattern pat = Pattern.compile("(myclinic-release-\\d+-\\d+)\\.txt");
        DirectoryStream<Path> paths =  Files.newDirectoryStream(releaseDirPath);
        for(Path path: paths) {
            Path fileName = path.getFileName();
            Matcher matcher = pat.matcher(fileName.toString());
            if( matcher.matches() ){
                return matcher.group(1);
            }
        }
        return null;
    }

}
