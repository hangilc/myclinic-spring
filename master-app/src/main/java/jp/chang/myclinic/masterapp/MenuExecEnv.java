package jp.chang.myclinic.masterapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
		if( masterFilesDirectory == null ){
			masterFilesDirectory = Paths.get(masterFilesConfig);
		}
		return masterFilesDirectory;
	}
}