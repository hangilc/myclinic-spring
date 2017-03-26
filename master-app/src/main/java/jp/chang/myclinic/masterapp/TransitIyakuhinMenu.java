package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.format.DateTimeParseException;
import jp.chang.myclinic.master.transit.IyakuhinMaster;

public class TransitIyakuhinMenu implements Menu {

	private Menu parentMenu;
	private IyakuhinMaster masterFrom;
	private IyakuhinMaster masterTo;
	private String validFrom;

	public TransitIyakuhinMenu(Menu parentMenu){
		this.parentMenu = parentMenu;
		this.validFrom = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public void setMasterFrom(IyakuhinMaster masterFrom){
		this.masterFrom = masterFrom;
	}

	public void setMasterTo(IyakuhinMaster masterTo){
		this.masterTo = masterTo;
	}

	@Override
	public String getPrompt(){
		return "transit-iyakuhin>";
	}

	@Override
	public void printMessage(MenuExecEnv env){
		if( masterFrom == null ){
			env.out.println("master-from: (not selected)");
		} else {
			env.out.printf("master-from: %s (%d)\n", masterFrom.name, masterFrom.iyakuhincode);
		}
		if( masterTo == null ){
			env.out.println("master-to: (not selected)");
		} else {
			env.out.printf("master-to: %s (%d)\n", masterTo.name, masterTo.iyakuhincode);
		}
		env.out.printf("valid-from: %s\n", validFrom);
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("from",
			"selects a master from which to transit",
			"syntax: from",
			(arg, env) -> new TransitIyakuhinFromMenu(this, validFrom)
		));
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			(arg, env) -> parentMenu
		));
		return commands;
	}

}