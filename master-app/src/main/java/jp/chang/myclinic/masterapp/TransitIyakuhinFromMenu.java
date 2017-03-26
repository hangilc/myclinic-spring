package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;
import org.springframework.jdbc.core.JdbcTemplate;
import jp.chang.myclinic.master.transit.IyakuhinMaster;

public class TransitIyakuhinFromMenu implements Menu {

	private Menu parentMenu;
	private List<IyakuhinMaster> selections;
	private IyakuhinMaster selectedMaster;

	public TransitIyakuhinFromMenu(Menu parentMenu){
		this.parentMenu = parentMenu;
		selections = new ArrayList<IyakuhinMaster>();
	}

	@Override
	public String getPrompt(){
		return "transit-iyakuhin-from>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("search",
			"searches master-from in iyakuhin master",
			new String[]{
				"syntax: search text...", 
				"  text -- text to search",
				"          Space separated multiple texts can be specified,", 
				"          in which case iyakuhin names including all the texts",
				"          in that order are searched."
			},
			this::doSearch
		));
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			(arg, env) -> parentMenu
		));
		return commands;
	}

	private Menu doSearch(String arg, MenuExecEnv env){
		String[] texts = arg.split("(?U:\\s+)");
		if( texts.length > 0 ){
			List<IyakuhinMaster> list = searchIyakuhin(texts, env);
			selections.clear();
			selections.addAll(list);
			printSelections(env);
		}
		return this;
	}

    private List<IyakuhinMaster> searchIyakuhin(String[] texts, MenuExecEnv env){
    	List<IyakuhinMaster> list = new ArrayList<>();
    	String text = "%" + String.join("%", texts) + "%";
    	JdbcTemplate jdbcTemplate = env.getJdbcTemplate();
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

	private void printSelections(MenuExecEnv env){
		for(int i=0;i<selections.size();i++){
			IyakuhinMaster m = selections.get(i);
			env.out.printf("%2d.%s\n", i+1, m.name);
		}
	}

}