package jp.chang.myclinic.management;

import jp.chang.myclinic.winutil.ShellLink;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateShortcuts {

    //private static Logger logger = LoggerFactory.getLogger(CreateShortcuts.class);

    public static void main(String[] args) throws Exception {
        String suffix = "";
        if( args.length == 3 || args.length == 4 ) {
            if( args.length == 4 ){
                suffix = args[3];
            }
            new CreateShortcuts(args[0], Paths.get(args[1]), Paths.get(args[2]), suffix).run();
        } else {
            System.err.println("Usage; CreateShortcuts role release-folder target-folder [suffix]");
        }
    }

    private String role;
    private Path releaseDir;
    private Path targetDir;
    private String suffix = "";
    private Path javaHome;

    private CreateShortcuts(String role, Path releaseDir, Path targetDir, String suffix) {
        this.role = role;
        this.releaseDir = releaseDir;
        this.targetDir = targetDir;
        this.javaHome = Paths.get(System.getenv("JAVA_HOME"), "bin");
    }

    private void run(){
        switch(role){
            case "practice": {
                runPractice();
                break;
            }
            default: {
                System.err.println("Invalid role: " + role);
                System.exit(1);
            }
        }
    }

    private void runPractice(){
        createShortcut("java", "-jar server.jar", "サーバー");
        createShortcut("javaw", "-jar hotline.jar practice reception", "ホットライン（受付）");
        createShortcut("javaw", "-jar hotline.jar practice pharmacy", "ホットライン（薬局）");
        createShortcut("javaw", "-jar reception.jar", "受付");
        createShortcut("javaw", "-jar practice.jar", "診察");
        createShortcut("javaw", "-jar intraclinic.jar", "院内ミーティング");
        createShortcut("javaw", "-jar pharma.jar", "薬局");
        createShortcut("javaw", "-jar record-browser.jar", "診療録");
    }

    private Path javaPath(String exec){
        return javaHome.resolve(exec);
    }

    private void createShortcut(String java, String arguments, String linkName){
        ShellLink shellLink = new ShellLink();
        shellLink.setPath(javaPath(java).toString());
        shellLink.setArguments(arguments);
        shellLink.setWorkingDirectory(releaseDir.toAbsolutePath().toString());
        shellLink.save(targetDir.resolve(linkName).toString() + suffix + ".lnk");
        shellLink.close();
    }

}
