package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;
import org.springframework.jdbc.core.JdbcTemplate;
import jp.chang.myclinic.master.transit.IyakuhinMaster;

public class TransitIyakuhinToMenu implements Menu {

	private TransitIyakuhinMenu parentMenu;
	private String validFrom;
	private List<IyakuhinMaster> selections;
	private IyakuhinMaster selectedMaster;

	public TransitIyakuhinToMenu(TransitIyakuhinMenu parentMenu, String validFrom){
		this.parentMenu = parentMenu;
		this.validFrom = validFrom;
		selections = new ArrayList<IyakuhinMaster>();
	}

	@Override
	public String getPrompt(){
		return "transit-iyakuhin-to>";
	}

	@Override
	public void printMessage(MenuExecEnv env){
		IyakuhinMaster m = selectedMaster;
    	if( m != null ){
    		System.out.printf("current master-to: %s (%d)\n", m.name, m.iyakuhincode);
    	} else {
    		System.out.println("current master-to: (none)");
    	}
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("search",
			"searches master-to in iyakuhin master",
			new String[]{
				"syntax: search text...", 
				"  text -- text to search",
				"          Space separated multiple texts can be specified,", 
				"          in which case iyakuhin names including all the texts",
				"          in that order are searched."
			},
			this::doSearch
		));
		commands.add(Command.create("select",
			"selects master-to",
			new String[]{
				"syntax: select number",
				"  number -- index number of searched iyakuhin"
			},
			this::doSelect
		));
		commands.add(Command.create("done",
			"accepts the current selection as master-to",
			"syntax: done",
			this::doDone
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

	private Menu doSelect(String arg, MenuExecEnv env){
		try {
			int index = Integer.parseInt(arg);
			selectedMaster = selections.get(index-1);
		} catch(NumberFormatException ex){
			env.out.println("invalid number format");
		} catch(IndexOutOfBoundsException ex){
			env.out.println("invalid index number");
		}
		return this;
	}

	private Menu doDone(String arg, MenuExecEnv env){
		if( selectedMaster == null ){
			env.out.println("no master selected");
			return this;
		} else {
			parentMenu.setMasterTo(selectedMaster);
			return parentMenu;
		}
	}

    private List<IyakuhinMaster> searchIyakuhin(String[] texts, MenuExecEnv env){
    	List<IyakuhinMaster> list = new ArrayList<>();
    	String text = "%" + String.join("%", texts) + "%";
    	JdbcTemplate jdbcTemplate = env.getJdbcTemplate();
    	jdbcTemplate.query("select * from iyakuhin_master_arch where " +
    		" name like ? and " + 
    		" valid_from <= ? and " +
    		" (valid_upto = '0000-00-00' or ? <= valid_upto) " +
    		" limit 100 ",
    		new Object[]{ text, validFrom, validFrom },
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