package jp.chang.myclinic;

import java.util.List;
import java.util.ArrayList;

class MainMenu extends Menu {
	@Override
	protected String getPrompt(){
		return "main>";
	}

	@Override
	protected List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(new IyakuhinCommand());
		commands.add(new Menu.ExitCommand());
		return commands;
	}

	private static class IyakuhinCommand implements Command {
		@Override
		public String getName(){
			return "iyakuhin";
		}

		@Override
		public String getDescription(){
			return "adds iyakuhin master";
		}

		@Override
		public String getDetail(){
			return "syntax: iyakuhin";
		}

		@Override
		public Menu exec(String arg){
			return new IyakuhinAddMenu();
		}
	}
}