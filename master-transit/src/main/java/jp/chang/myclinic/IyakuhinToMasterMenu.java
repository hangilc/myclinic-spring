package jp.chang.myclinic;

class IyakuhinToMenu extends IyakuhinSearchBase {

	IyakuhinToMenu(IyakuhinMenu parentMenu){
		super(parentMenu);
		addCommand(new SearchCommand(this));
		addCommand(new SelectCommand(this, "selects master-to from search result"));
		addCommand(new DoneCommand(this));
		addCommand(new CancelCommand("cancels choosing master-to"));
	}

	@Override
	public String getPrompt(){
		return "iyakuhin-to>";
	}

	@Override
	public void printMessage(){
		IyakuhinMaster m = getCurrentSelection();
    	if( m != null ){
    		System.out.printf("current master-to: %s (%d)\n", m.name, m.iyakuhincode);
    	} else {
    		System.out.println("current master-to: (no selection)");
    	}
	}

	@Override
	public Menu handleDone(IyakuhinMaster master){
		parentMenu.setMasterTo(master);
		return parentMenu;
	}

}