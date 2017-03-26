package jp.chang.myclinic;

import java.io.IOException;
import jp.chang.myclinic.masterapp.MenuDriver;
import jp.chang.myclinic.masterapp.MainMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class MasterApplication implements CommandLineRunner {
    public static void main( String[] args ) throws IOException {
    	SpringApplication.run(MasterApplication.class, args);
    }

    @Autowired
    public MenuDriver menuDriver;

    @Override
    public void run(String[] args) throws IOException {
        menuDriver.run(new MainMenu());
    }
}
