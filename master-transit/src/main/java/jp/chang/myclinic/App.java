package jp.chang.myclinic;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication 
public class App implements CommandLineRunner {
    public static void main( String[] args ) throws IOException
    {
    	SpringApplication.run(App.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void run(String[] args) throws IOException {
        JdbcUtil.jdbcTemplate = jdbcTemplate;
        Menu menu = new MainMenu();
        while( menu != null ){
            menu = menu.exec();
        }
    }

}

