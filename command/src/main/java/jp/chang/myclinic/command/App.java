package jp.chang.myclinic.command;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import jp.chang.myclinic.model.Patient;
import jp.chang.myclinic.model.PatientRepository;

@EntityScan(basePackages={"jp.chang.myclinic.model"})
@EnableJpaRepositories(basePackages={"jp.chang.myclinic.model"})
@SpringBootApplication
public class App implements CommandLineRunner {
	@Autowired PatientRepository patientRepository;

	public static void main(String[] args){
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("hello, world");
		Patient patient = patientRepository.findOne(198);
		System.out.println(patient);
	}
}