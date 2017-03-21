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
		commands.add(new Menu.ExitCommand());
		return commands;
	}
}