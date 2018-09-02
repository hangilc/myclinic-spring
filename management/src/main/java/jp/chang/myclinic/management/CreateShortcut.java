package jp.chang.myclinic.management;

import jp.chang.myclinic.winutil.ShellLink;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateShortcut {

    public static void main(String[] args) {
        if ( !(args.length >= 2 && args.length <= 4) ) {
            System.err.println("Usage: create-shortcut SAVE-PATH TARGET [ARGS] [WORKING-DIR]");
            System.exit(1);
        }
        String savePath = args[0];
        String target = args[1];
        String arguments = null;
        String workDir = null;
        if( args.length >= 3 ){
            arguments = args[2];
            if( args.length >= 4 ){
                workDir = args[3];
            }
        }
        System.err.println("savePath: " + savePath);
        System.err.println("target: " + target);
        System.err.println("arguments: " + arguments);
        System.err.println("workDir: " + workDir);
        ShellLink shellLink = new ShellLink();
        Path targetPath = Paths.get(target);
        shellLink.setPath(targetPath.toAbsolutePath().toString());
        if( arguments != null ) {
            shellLink.setArguments(arguments);
        }
        if( workDir != null ) {
            Path workDirPath = Paths.get(workDir);
            shellLink.setWorkingDirectory(workDirPath.toAbsolutePath().toString());
        }
        shellLink.save(Paths.get(savePath).toAbsolutePath().toString());
        shellLink.close();
    }

}

