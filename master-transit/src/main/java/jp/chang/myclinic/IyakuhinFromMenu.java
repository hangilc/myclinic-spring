package jp.chang.myclinic;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.jdbc.core.JdbcTemplate;

class IyakuhinFromMenu extends Menu {

	private IyakuhinMenu parentMenu;
	private List<Command> commands;
	private List<IyakuhinMaster> selections;
	private IyakuhinMaster selectedMaster;

	IyakuhinFromMenu(IyakuhinMenu parentMenu){
		this.parentMenu = parentMenu;
		commands = new ArrayList<Command>();
		commands.add(new SearchCommand());
		commands.add(new SelectCommand());
		commands.add(new DoneCommand());
		commands.add(new CancelCommand());
		selections = new ArrayList<IyakuhinMaster>();
	}

	private class SearchCommand implements Command {
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
			return IyakuhinFromMenu.this;
		}

		private void printSelections(){
			for(int i=0;i<selections.size();i++){
				IyakuhinMaster m = selections.get(i);
				System.out.printf("%2d.%s\n", i+1, m.name);
			}
		}
	}

	private class SelectCommand implements Command {
		@Override
		public String getName(){
			return "select";
		}

		@Override
		public String getDescription(){
			return "selects iyakuhin transit from";
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
				printSelectedMaster();
				return IyakuhinFromMenu.this;
			} catch(NumberFormatException ex){
				System.out.println("invalid number format");
				return IyakuhinFromMenu.this;
			} catch(IndexOutOfBoundsException ex){
				System.out.println("invalid index number");
				return IyakuhinFromMenu.this;
			}
		}
	}

	private class DoneCommand implements Command {
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
				return IyakuhinFromMenu.this;
			}
			parentMenu.setMasterFrom(selectedMaster);
			return parentMenu;
		}
	}

	private class CancelCommand implements Command {
		@Override
		public String getName(){
			return "cancel";
		}

		@Override
		public String getDescription(){
			return "cancels choosing iyakuhin transit from";
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
	public String getPrompt(){
		return "iyakuhin-from>";
	}

	@Override
	public List<Command> getCommands(){
		return commands;
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

    private void printSelectedMaster(){
    	if( selectedMaster != null ){
    		IyakuhinMaster m = selectedMaster;
    		System.out.printf("current from: %s (%d)\n", m.name, m.iyakuhincode);
    	} else {
    		System.out.println("no selection");
    	}
    }

}