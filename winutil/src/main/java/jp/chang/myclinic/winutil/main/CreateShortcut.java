package jp.chang.myclinic.winutil.main;

import jp.chang.myclinic.winutil.ShellLink;

public class CreateShortcut {

    public static void main( String[] args )
    {
        ShellLink shellLink = new ShellLink();
        shellLink.setPath("C:/Program Files/erl8.3/bin/werl.exe");
        System.out.println(shellLink.getPath());
        shellLink.setWorkingDirectory("C:/Users/hangil/work/learn-java/myclinic-spring");
        shellLink.save("C:/Users/hangil/shortcut.lnk");
        shellLink.close();
    }

}

