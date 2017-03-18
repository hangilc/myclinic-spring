package jp.chang.myclinic;

import java.io.IOException;
import java.io.PrintWriter;

import jline.console.ConsoleReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication 
public class App implements CommandLineRunner
{
    public static void main( String[] args ) throws IOException
    {
    	SpringApplication.run(App.class, args);
    }

    public void run(String[] args) throws IOException {
    	ConsoleReader reader = new ConsoleReader();
    	reader.getCursorBuffer().write("pqr");
    	String line = reader.readLine(">");
    	reader.println(line);
    	reader.flush();
    }
}
