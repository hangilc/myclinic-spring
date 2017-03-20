package jp.chang.myclinic;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.format.DateTimeParseException;

class IyakuhinMenu extends Menu {

	private MainMenu mainMenu;
	private List<Command> commands;
	private IyakuhinMaster masterFrom;
	private IyakuhinMaster masterTo;
	private String validFrom;

	IyakuhinMenu(MainMenu mainMenu){
		this.mainMenu = mainMenu;
		commands = new ArrayList<Command>();
		commands.add(new FromCommand());
		commands.add(new ToCommand());
		commands.add(new SetValidFromCommand());
		commands.add(new PrintCommand());
		commands.add(new CancelCommand());
		commands.add(new ReturnCommand());
		validFrom = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	void setMasterFrom(IyakuhinMaster masterFrom){
		this.masterFrom = masterFrom;
	}

	void setMasterTo(IyakuhinMaster masterTo){
		this.masterTo = masterTo;
	}

	private Menu currentMenu(){
		return this;
	}

	private class FromCommand implements Command {
		@Override
		public String getName(){
			return "from";
		}

		@Override
		public String getDescription(){
			return "chooses iyakuhin from which to transit";
		}

		@Override
		public String getDetail(){
			return "syntax: from";
		}

		@Override
		public Menu exec(String arg){
			return new IyakuhinFromMenu(IyakuhinMenu.this);
		}
	}

	private class ToCommand implements Command {
		@Override
		public String getName(){
			return "to";
		}

		@Override
		public String getDescription(){
			return "chooses iyakuhin to which to transit";
		}

		@Override
		public String getDetail(){
			return "syntax: to";
		}

		@Override
		public Menu exec(String arg){
			return new IyakuhinToMenu(IyakuhinMenu.this);
		}
	}

	private class SetValidFromCommand implements Command {
		@Override
		public String getName(){
			return "set-valid-from";
		}

		@Override
		public String getDescription(){
			return "sets valid-from";
		}

		@Override
		public String getDetail(){
			return "syntax: set-valid-from at\n" +
				"  at - date in YYYY-MM-DD format";
		}

		@Override
		public Menu exec(String arg){
			boolean matches = Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", arg);
			if( matches ){
				DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
				formatter = formatter.withResolverStyle(ResolverStyle.STRICT);
				try {
					LocalDate d = LocalDate.parse(arg, formatter);
					validFrom = d.format(DateTimeFormatter.ISO_LOCAL_DATE);
				} catch(DateTimeParseException ex){
					System.out.println("inappropriate valid-from: " + arg);
				}
			} else {
				System.out.println("invalid valid-from: " + arg);
			}
			return currentMenu();
		}
	}

	private class PrintCommand implements Command {
		@Override
		public String getName(){
			return "print";
		}

		@Override
		public String getDescription(){
			return "prints a line for master-map";
		}

		@Override
		public String getDetail(){
			return "syntax: print";
		}

		@Override
		public Menu exec(String arg){
			if( masterFrom == null ){
				System.out.println("master-from is not selected");
			} else if( masterTo == null ){
				System.out.println("master-to is not selected");
			} else {
				System.out.printf("Y,%d,%s,%d %s -> %s\n",
					masterFrom.iyakuhincode, validFrom, masterTo.iyakuhincode,
					masterFrom.name, masterTo.name);
			}
			return currentMenu();
		}
	}

	private class CancelCommand implements Command {
		@Override public String getName(){ return "cancel"; }
		@Override public Menu exec(String arg){ return mainMenu; }
		@Override public String getDescription(){ return "cancels iyakuhin transit"; }
		@Override public String getDetail(){ return "syntax: cancel"; }
	}

	private class ReturnCommand implements Command {
		@Override public String getName(){ return "return"; }
		@Override public Menu exec(String arg){ return mainMenu; }
		@Override public String getDescription(){ return "returns to main menu"; }
		@Override public String getDetail(){ return "syntax: return"; }
	}

	@Override
	protected String getPrompt(){
		return "iyakuhin>";
	}

	@Override
	protected List<Command> getCommands(){
		return commands;
	}

	@Override
	protected void printMessage(){
		System.out.println();
		if( masterFrom == null ){
			System.out.println("master-from: (not selected)");
		} else {
			System.out.printf("master-from: %s (%d)\n", masterFrom.name, masterFrom.iyakuhincode);
		}
		if( masterTo == null ){
			System.out.println("master-to: (not selected)");
		} else {
			System.out.printf("master-to: %s (%d)\n", masterTo.name, masterTo.iyakuhincode);
		}
		System.out.printf("valid-from: %s\n", validFrom);
	}
		
}