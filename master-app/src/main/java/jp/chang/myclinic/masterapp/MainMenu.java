package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;

class MainMenu extends Menu {

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
			arg -> new DownloadMenu(this)
		));
		commands.add(Command.create("exit",
			"exits this program",
			"syntax: exit",
			arg -> null
		));
		return commands;
	}

}