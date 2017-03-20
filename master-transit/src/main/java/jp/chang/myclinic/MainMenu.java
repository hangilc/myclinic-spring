package jp.chang.myclinic;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

class MainMenu extends Menu {

	private List<Command> commands;

	MainMenu(){
		commands = new ArrayList<Command>();
		commands.add(new IyakuhinCommand(this));
		commands.add(new ExitCommand());
	}

	private static class IyakuhinCommand implements Command {

		private MainMenu mainMenu;

		IyakuhinCommand(MainMenu mainMenu){
			this.mainMenu = mainMenu;
		}

		@Override
		public String getName(){
			return "iyakuhin";
		}

		@Override
		public String getDescription(){
			return "adds iyakuhin transit";
		}

		@Override
		public String getDetail(){
			return "syntax: iyakuhin";
		}

		@Override
		public Menu exec(String arg){
			return new IyakuhinMenu(mainMenu);
		}
		
	}

	private static class ExitCommand implements Command {

		@Override
		public String getName(){
			return "exit";
		}

		@Override
		public String getDescription(){
			return "exits this program";
		}

		@Override
		public String getDetail(){
			return "syntax: exit";
		}

		@Override
		public Menu exec(String arg){
			return null;
		}

	}

	@Override
	public List<Command> getCommands(){
		return commands;
	}

	@Override
	protected String getPrompt(){
		return "main>";
	}

}