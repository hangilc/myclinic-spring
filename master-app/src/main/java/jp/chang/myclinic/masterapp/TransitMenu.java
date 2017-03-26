package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;

public class TransitMenu implements Menu {

	private Menu parentMenu;

	public TransitMenu(Menu parentMenu){
		this.parentMenu = parentMenu;
	}

	@Override
	public String getPrompt(){
		return "transit>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("transit-iyakuhin",
			"adds iyakuhin code transit",
			"syntax: transit-iyakuhin",
			(arg, env) -> new TransitIyakuhinMenu(this)
		));
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			(arg, env) -> parentMenu
		));
		return commands;
	}

}