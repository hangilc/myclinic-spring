package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;

class DownloadMenu extends Menu {

	private Menu parentMenu;

	DownloadMenu(Menu parentMenu){
		this.parentMenu = parentMenu;
	}

	@Override
	public String getPrompt(){
		return "download>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			arg -> parentMenu
		));
		return commands;
	}

}