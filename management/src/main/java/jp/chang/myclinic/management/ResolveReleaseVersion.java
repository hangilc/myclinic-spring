package jp.chang.myclinic.management;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResolveReleaseVersion {

    //private static Logger logger = LoggerFactory.getLogger(ResolveReleaseVersion.class);

    public static void main(String[] args) throws Exception {
        if( args.length != 1 ){
            System.err.println("Usage: ResolveReleaseVersion release-folder");
            System.exit(1);
        }
        Path folder = Paths.get(args[0]);
        String ver = null;
        for(Path path : Files.newDirectoryStream(folder)){
            String v = getVersion(path);
            if( v != null ){
                ver = v;
                break;
            }
        }
        System.out.println("version: " + ver);
    }

    private static String getVersion(Path path){
        Path fileName = path.getFileName();
        Pattern pat = Pattern.compile("myclinic-release-\\d+-\\d+\\.txt");
        Matcher matcher = pat.matcher(fileName.toString());
        if( matcher.matches() ){
            return fileName.toString();
        } else {
            return null;
        }
    }
}
