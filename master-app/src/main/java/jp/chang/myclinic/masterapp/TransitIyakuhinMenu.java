package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
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
			"selects a master-from (master entry from which to transit)",
			"syntax: from",
			(arg, env) -> new TransitIyakuhinFromMenu(this, validFrom)
		));
		commands.add(Command.create("to",
			"selects a master-to (master entry to which to transit)",
			"syntax: to",
			(arg, env) -> new TransitIyakuhinToMenu(this, validFrom)
		));
		commands.add(Command.create("set-valid-from",
			"sets valid-from",
			new String[]{
				"syntax: set-valid-from at",
				"  at - date in YYYY-MM-DD format"
			},
			this::doSetValidFrom
		));
		commands.add(Command.create("print",
			"prints a line for master-map",
			"syntax: print",
			this::doPrint
		));
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			(arg, env) -> parentMenu
		));
		return commands;
	}

	private Menu doSetValidFrom(String arg, MenuExecEnv env){
		boolean matches = Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", arg);
		if( matches ){
			DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
			formatter = formatter.withResolverStyle(ResolverStyle.STRICT);
			try {
				LocalDate d = LocalDate.parse(arg, formatter);
				validFrom = d.format(DateTimeFormatter.ISO_LOCAL_DATE);
			} catch(DateTimeParseException ex){
				env.out.println("inappropriate valid-from: " + arg);
			}
		} else {
			env.out.println("invalid valid-from: " + arg);
		}
		return this;
	}

	private Menu doPrint(String arg, MenuExecEnv env){
		if( masterFrom == null ){
			env.out.println("master-from is not selected");
		} else if( masterTo == null ){
			env.out.println("master-to is not selected");
		} else {
			env.out.printf("Y,%d,%s,%d %s -> %s\n",
				masterFrom.iyakuhincode, validFrom, masterTo.iyakuhincode,
				masterFrom.name, masterTo.name);
		}
		return this;
	}
}