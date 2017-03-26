package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;

public class MainMenu implements Menu {

	@Override
	public String getPrompt(){
		return "main>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("download",
			"downalods master files",
			"syntax: download",
			(arg, env) -> new DownloadMenu(this)
		));
		commands.add(Command.create("exit",
			"exits the program",
			"syntax: exit",
			(arg, env) -> null
		));
		return commands;
	}

}