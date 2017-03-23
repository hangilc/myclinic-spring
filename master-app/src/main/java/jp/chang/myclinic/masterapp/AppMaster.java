package jp.chang.myclinic.masterapp;

import java.io.IOException;

public class AppMaster
{
    public static void main( String[] args ) throws IOException {
        Menu menu = new MainMenu();
        while( menu != null ){
        	menu = menu.exec();
        }
    }
}
