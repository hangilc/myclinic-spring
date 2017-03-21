package jp.chang.myclinic;

import java.util.List;
import java.util.ArrayList;

class IyakuhinAddMenu extends Menu {
	@Override
	public String getPrompt(){
		return "iyakuhin-add>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();

		return commands;
	}
}