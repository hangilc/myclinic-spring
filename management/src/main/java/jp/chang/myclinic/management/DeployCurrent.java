package jp.chang.myclinic.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.file.FileVisitResult.CONTINUE;

// Usage: DeployCurrent role release-dir
public class DeployCurrent {

    private static Logger logger = LoggerFactory.getLogger(DeployCurrent.class);
    private String role;
    private Path targetDir;
    private Path baseDir;

    public static void main(String[] args) throws Exception {
        new DeployCurrent(args).run();
    }

    private DeployCurrent(String[] args) {
        if( args.length == 2 ){
            this.role = args[0];
            this.targetDir = Paths.get(args[1]);
        } else {
            System.err.println("Usage: DeployCurrent role [release-dir]");
        }
        this.baseDir = Paths.get(System.getProperty("user.dir"));
    }

    private void run() throws IOException {
        switch(role){
            case "practice": {
                runPractice();
                break;
            }
            case "reception": {
                runReception();
                break;
            }
            case "pharma": {
                runPharma();
                break;
            }
            default: {
                System.err.println("Invalid role: " + role);
                System.exit(1);
            }
        }
    }

    private void runPractice() throws IOException {
        ensureTargetDir();
        copyRecursively(
                baseDir.resolve("config"),
                targetDir.resolve("config")
        );
        copyJar("hotline");
        copyJar("intraclinic");
        copyJar("pharma");
        copyJar("practice");
        copyJar("reception");
        copyJar("record-browser");
        copyJar("scanner");
        copyJar("server");
        copyJar("rcpt-drawer");
    }

    private void runReception() throws IOException {
        ensureTargetDir();
        copyJar("hotline");
        copyJar("intraclinic");
        copyJar("reception");
        copyJar("record-browser");
        copyJar("scanner");
    }

    private void runPharma() throws IOException {
        ensureTargetDir();
        copyJar("hotline");
        copyJar("intraclinic");
        copyJar("pharma");
        copyJar("record-browser");
    }

    private void ensureTargetDir() throws IOException {
        if (Files.exists(targetDir)) {
            moveTargetDir();
        }
        Files.createDirectory(targetDir);
    }

    private void copyJar(String name) throws IOException {
        Files.copy(
                baseDir.resolve(String.format("%s/target/%s-1.0.0-SNAPSHOT.jar", name, name)),
                targetDir.resolve(name + ".jar")
        );
    }

    private void moveTargetDir() throws IOException {
        String fname = targetDir.toString() + "-" + getTimeStamp();
        Path newPath = Paths.get(fname);
        Files.move(targetDir, newPath);
    }

    private String getTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uMMdd-HHmmss");
        return now.format(format);
    }

    private Path createDestPath(Path srcDir, Path srcFile, Path destDir){
        Path relative = srcDir.relativize(srcFile);
        return destDir.resolve(relative);
    }

    private void copyRecursively(Path srcDir, Path dstDir) throws IOException {
        Files.createDirectory(dstDir);
        Files.walkFileTree(srcDir, new FileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path newDir = createDestPath(srcDir, dir, dstDir);
                Files.createDirectories(newDir);
                return CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, createDestPath(srcDir, file, dstDir));
                return CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                logger.error("copyRecursively failed. {}", exc);
                return CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return CONTINUE;
            }
        });
    }

}
