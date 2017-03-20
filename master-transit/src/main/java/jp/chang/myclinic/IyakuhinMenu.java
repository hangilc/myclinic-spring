package jp.chang.myclinic;

import java.util.List;
import java.util.ArrayList;

class IyakuhinMenu extends Menu {

	private MainMenu mainMenu;
	private List<Command> commands;
	private IyakuhinMaster masterFrom;
	private IyakuhinMaster masterTo;

	IyakuhinMenu(MainMenu mainMenu){
		this.mainMenu = mainMenu;
		commands = new ArrayList<Command>();
		commands.add(new FromCommand());
		commands.add(new CancelCommand());
	}

	void setMasterFrom(IyakuhinMaster masterFrom){
		this.masterFrom = masterFrom;
	}

	void setMasterTo(IyakuhinMaster masterTo){
		this.masterTo = masterTo;
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
	}
		
}