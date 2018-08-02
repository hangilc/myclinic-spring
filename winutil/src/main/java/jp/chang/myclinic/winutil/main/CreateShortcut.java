package jp.chang.myclinic.winutil.main;

import jp.chang.myclinic.winutil.ShellLink;

public class CreateShortcut {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Usage: create-shortcut TARGET ARGS WORKING-DIR SAVE-PAT");
            System.exit(1);
        }
        String target = args[0];
        String arguments = args[1];
        String workDir = args[2];
        String savePath = args[3];
        ShellLink shellLink = new ShellLink();
        shellLink.setPath(target);
        shellLink.setArguments(arguments);
        shellLink.setWorkingDirectory(workDir);
        shellLink.save(savePath);
        shellLink.close();
    }

}

