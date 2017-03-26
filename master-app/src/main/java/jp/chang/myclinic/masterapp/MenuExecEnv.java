package jp.chang.myclinic.masterapp;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;

@Component
public class MenuExecEnv {

	public PrintStream out;
	private BufferedReader reader;
	@Value("${myclinic.master-files-directory:master-files}")
	private String masterFilesConfig;
	private Path masterFilesDirectory;

	public MenuExecEnv(){
		out = System.out;
		InputStreamReader r = new InputStreamReader(System.in);
		reader = new BufferedReader(r);
	}

	@PostConstruct
	public void postConstruct(){
		masterFilesDirectory = Paths.get(masterFilesConfig);
	}

	public String readLine(String prompt){
		out.print(prompt);
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