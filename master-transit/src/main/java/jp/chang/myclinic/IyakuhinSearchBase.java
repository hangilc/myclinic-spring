package jp.chang.myclinic;

import java.util.List;
import java.util.ArrayList;

import org.springframework.jdbc.core.JdbcTemplate;

abstract class IyakuhinSearchBase extends Menu {

	protected IyakuhinMenu parentMenu;
	private Menu currentMenu;
	private List<Command> commands;
	private List<IyakuhinMaster> selections;
	private IyakuhinMaster selectedMaster;

	IyakuhinSearchBase(IyakuhinMenu parentMenu){
		this.parentMenu = parentMenu;
		commands = new ArrayList<Command>();
		selections = new ArrayList<IyakuhinMaster>();
	}

	protected void addCommand(Command command){
		this.commands.add(command);
	}

	protected IyakuhinMaster getCurrentSelection(){
		return selectedMaster;
	}

	protected class SearchCommand implements Command {

		private Menu currentMenu;

		SearchCommand(Menu currentMenu){
			this.currentMenu = currentMenu;
		}

		@Override
		public String getName(){
			return "search";
		}

		@Override
		public String getDescription(){
			return "searches in iyakuhin master";
		}

		@Override
		public String getDetail(){
			return "syntax: search text...\n" + 
				"  text -- text to search\n" +
				"          Space separated multiple texts can be specified,\n" + 
				"          in which case iyakuhin names including all the texts\n" +
				"          in that order are searched.\n";
		}

		@Override
		public Menu exec(String arg){
			String[] texts = arg.split("(?U:\\s+)");
			if( texts.length > 0 ){
				List<IyakuhinMaster> list = searchIyakuhin(texts);
				selections.clear();
				selections.addAll(list);
				printSelections();
			}
			return currentMenu;
		}

	}

	protected class SelectCommand implements Command {

		private Menu currentMenu;
		private String description;

		SelectCommand(Menu currentMenu, String description){
			this.currentMenu = currentMenu;
			this.description = description;
		}

		@Override
		public String getName(){
			return "select";
		}

		@Override
		public String getDescription(){
			return description;
		}

		@Override
		public String getDetail(){
			return "syntax: select number\n" +
				"  number -- index number of searched iyakuhin";
		}

		@Override
		public Menu exec(String arg){
			try {
				int index = Integer.parseInt(arg);
				selectedMaster = selections.get(index-1);
			} catch(NumberFormatException ex){
				System.out.println("invalid number format");
			} catch(IndexOutOfBoundsException ex){
				System.out.println("invalid index number");
			}
			return currentMenu;
		}
	}

	protected class DoneCommand implements Command {
		private Menu currentMenu;

		DoneCommand(Menu currentMenu){
			this.currentMenu = currentMenu;
		}

		@Override
		public String getName(){
			return "done";
		}

		@Override
		public String getDescription(){
			return "accepts current selection";
		}

		@Override
		public String getDetail(){
			return "syntax: done\n";
		}

		@Override
		public Menu exec(String arg){
			if( selectedMaster == null ){
				System.out.println("no master selected");
				return currentMenu;
			}
			return handleDone(selectedMaster);
		}
	}

	protected class CancelCommand implements Command {

		private String description;

		CancelCommand(String description){
			this.description = description;
		}

		@Override
		public String getName(){
			return "cancel";
		}

		@Override
		public String getDescription(){
			return description;
		}

		@Override
		public String getDetail(){
			return "syntax: cancel";
		}

		@Override
		public Menu exec(String arg){
			return parentMenu;
		}
	}

	@Override
	public List<Command> getCommands(){
		return commands;
	}

	@Override
	public void printMessage(){
		IyakuhinMaster m = getCurrentSelection();
    	if( m != null ){
    		System.out.printf("current selection: %s (%d)\n", m.name, m.iyakuhincode);
    	} else {
    		System.out.println("current selection: (no selection)");
    	}
	}

    private List<IyakuhinMaster> searchIyakuhin(String[] texts){
    	List<IyakuhinMaster> list = new ArrayList<>();
    	String text = "%" + String.join("%", texts) + "%";
    	JdbcTemplate jdbcTemplate = JdbcUtil.jdbcTemplate;
    	jdbcTemplate.query("select * from iyakuhin_master_arch where " +
    		" name like ? and valid_upto = '0000-00-00' limit 100",
    		new Object[]{ text },
    		(row, rowNum) -> new IyakuhinMaster(
    			row.getInt("iyakuhincode"),
    			row.getString("valid_from"),
    			row.getString("name")
    		)
    	).forEach(master -> {
    		list.add(master);
    	});
    	return list;
    }

	private void printSelections(){
		for(int i=0;i<selections.size();i++){
			IyakuhinMaster m = selections.get(i);
			System.out.printf("%2d.%s\n", i+1, m.name);
		}
	}

	abstract protected Menu handleDone(IyakuhinMaster selection);

}