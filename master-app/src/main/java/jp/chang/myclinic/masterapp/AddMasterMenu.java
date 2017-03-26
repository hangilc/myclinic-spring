package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;

public class AddMasterMenu implements Menu {

	private Menu parentMenu;

	AddMasterMenu(Menu parentMenu){
		this.parentMenu = parentMenu;
	}

	@Override
	public String getPrompt(){
		return "add>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("add-iyakuhin",
			"adds iyakuhin master entry",
			"syntax: add-iyakuhin",
			(arg, env) -> new AddIyakuhinMenu(this, env)
		));
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			(arg, env) -> parentMenu
		));
		return commands;
	}

}