package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jp.chang.myclinic.master.add.MasterZipFinder;
import jp.chang.myclinic.master.add.CSVSearcher;
import jp.chang.myclinic.master.add.IyakuhinMasterCSV;
import jp.chang.myclinic.master.add.CommonsCSVRow;

public class AddIyakuhinMenu implements Menu {

	private Menu parentMenu;
	private Path zipFilePath;
	private IyakuhinMasterCSV selectedMaster;
	private String validFrom;

	public AddIyakuhinMenu(Menu parentMenu, MenuExecEnv env){
		this.parentMenu = parentMenu;
		Path masterFilesDirectory = env.getMasterFilesDirectory();
		this.zipFilePath = MasterZipFinder.findMasterZip(masterFilesDirectory, MasterZipFinder.IYAKUHIN_PREFIX);
		selections = new ArrayList<>();
		validFrom = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		System.out.println(zipFilePath);
	}

	@Override
	public String getPrompt(){
		return "add-iyakuhin>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			(arg, env) -> parentMenu
		));
		return commands;
	}

}
