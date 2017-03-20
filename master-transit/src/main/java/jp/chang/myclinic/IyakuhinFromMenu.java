package jp.chang.myclinic;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
class IyakuhinFromMenu extends Menu {

	private IyakuhinMenu parentMenu;
	private List<Command> commands;

    @Autowired
    JdbcTemplate jdbcTemplate;

	IyakuhinFromMenu(IyakuhinMenu parentMenu){
		this.parentMenu = parentMenu;
		commands = new ArrayList<Command>();
		commands.add(new SearchCommand());
		commands.add(new CancelCommand());
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
				list.stream().forEach(m -> {
					System.out.println(m.name);
				});
			}
			return IyakuhinFromMenu.this;
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

}