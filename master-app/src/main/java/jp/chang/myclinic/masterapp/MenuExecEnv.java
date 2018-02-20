package jp.chang.myclinic.masterapp;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class MenuExecEnv {

	public PrintStream out;
	private BufferedReader reader;
	@Value("${myclinic.master-files-directory:master-files}")
	private String masterFilesConfig;
	private Path masterFilesDirectory;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}

	public MenuExecEnv(){
		out = System.out;
		InputStreamReader r = new InputStreamReader(System.in);
		reader = new BufferedReader(r);
	}

	@PostConstruct
	public void postConstruct(){
		System.out.println("PostConstruct:" + masterFilesConfig);
		masterFilesDirectory = Paths.get(masterFilesConfig);
	}

	public String readLine(String prompt){
		out.print(prompt);
		return readLine();
	}

	public String readLine(){
		try {
			return reader.readLine();
		} catch(IOException ex){
			ex.printStackTrace(out);
			return "";
		}
	}

	public Path getMasterFilesDirectory(){
		return masterFilesDirectory;
	}
}