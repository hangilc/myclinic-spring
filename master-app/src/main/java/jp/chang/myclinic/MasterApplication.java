package jp.chang.myclinic;

import java.io.IOException;
import jp.chang.myclinic.masterapp.Menu;
import jp.chang.myclinic.masterapp.MainMenu;

public class MasterApplication
{
    public static void main( String[] args ) throws IOException {
    	System.out.println(jp.chang.myclinic.masterapp.Globals.getMasterFilesDirectory());
        Menu menu = new MainMenu();
        while( menu != null ){
        	menu = menu.exec();
        }
    }
}
