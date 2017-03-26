package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;
import jp.chang.myclinic.master.transit.IyakuhinMaster;

public class TransitIyakuhinMenu implements Menu {

	private Menu parentMenu;
	private IyakuhinMaster masterFrom;
	private IyakuhinMaster masterTo;
	private String validFrom;

	public TransitIyakuhinMenu(Menu parentMenu){
		this.parentMenu = parentMenu;
	}

	@Override
	public String getPrompt(){
		return "transit-iyakuhin>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("from",
			"selects a master from which to transit",
			"syntax: from",
			(arg, env) -> new TransitIyakuhinFromMenu(this)
		));
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			(arg, env) -> parentMenu
		));
		return commands;
	}

}