package jp.chang.myclinic;

import java.io.IOException;
import jp.chang.myclinic.masterapp.Menu;
import jp.chang.myclinic.masterapp.MainMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MasterApplication implements CommandLineRunner {
    public static void main( String[] args ) throws IOException {
    	SpringApplication.run(MasterApplication.class, args);
    }

    @Override
    public void run(String[] args) throws IOException {
        Menu menu = new MainMenu();
        while( menu != null ){
        	menu = menu.exec();
        }
    }
}
