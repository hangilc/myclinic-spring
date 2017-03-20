package jp.chang.myclinic;

import java.util.List;
import java.util.ArrayList;

class IyakuhinMenu extends Menu {

	private MainMenu mainMenu;
	private List<Command> commands;

	IyakuhinMenu(MainMenu mainMenu){
		this.mainMenu = mainMenu;
		commands = new ArrayList<Command>();
		commands.add(new FromCommand());
		commands.add(new CancelCommand());
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

	private class CancelCommand implements Command {
		@Override public String getName(){ return "cancel"; }
		@Override public Menu exec(String arg){ return mainMenu; }
		@Override public String getDescription(){ return "cancels iyakuhin transit"; }
		@Override public String getDetail(){ return "syntax: cancel"; }
	}

	@Override
	public String getPrompt(){
		return "iyakuhin>";
	}

	@Override
	public List<Command> getCommands(){
		return commands;
	}
		
}