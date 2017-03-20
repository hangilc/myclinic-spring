package jp.chang.myclinic;

class IyakuhinFromMenu extends IyakuhinSearchBase {

	IyakuhinFromMenu(IyakuhinMenu parentMenu){
		super(parentMenu);
		addCommand(new SearchCommand(this));
		addCommand(new SelectCommand(this, "selects master-from from search result"));
		addCommand(new DoneCommand(this));
		addCommand(new CancelCommand("cancels choosing master-from"));
	}

	@Override
	public String getPrompt(){
		return "iyakuhin-from>";
	}

	@Override
	public Menu handleDone(IyakuhinMaster master){
		parentMenu.setMasterFrom(master);
		return parentMenu;
	}

}
