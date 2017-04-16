package jp.chang.myclinic.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args){
		SpringApplication.run(Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... args){
		System.out.println("Hello");
	}
}