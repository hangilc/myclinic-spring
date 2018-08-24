package jp.chang.myclinic.management;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeployCurrent {

    //private static Logger logger = LoggerFactory.getLogger(DeployCurrent.class);
    private Path targetDir;

    public static void main(String[] args) throws Exception {
        new DeployCurrent(args).run();
    }

    private DeployCurrent(String[] args) {
        if( args.length > 0 ){
            this.targetDir = Paths.get(args[0]);
        } else {
            Path homeDir = Paths.get(System.getProperty("user.home"));
            this.targetDir = homeDir.resolve("myclinic-releases").resolve("current");
        }
    }

    private void run() throws IOException {
        if( Files.exists(targetDir) ){
            moveTargetDir();
        }
    }

    private void moveTargetDir() throws IOException {
        String fname = targetDir.toString() + "-" + getTimeStamp();
        Path newPath = Paths.get(fname);
        Files.move(targetDir, newPath);
    }

    private String getTimeStamp(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uMMdd-HHmmss");
        return now.format(format);
    }

}
