package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import org.apache.commons.csv.CSVRecord;
import org.springframework.jdbc.core.JdbcTemplate;

import jp.chang.myclinic.master.add.MasterZipFinder;
import jp.chang.myclinic.master.add.CSVSearcher;
import jp.chang.myclinic.master.add.IyakuhinMasterCSV;
import jp.chang.myclinic.master.add.CommonsCSVRow;

public class AddIyakuhinMenu implements Menu {

	private Menu parentMenu;
	private Path zipFilePath;
	private List<IyakuhinMasterCSV> selections;
	private IyakuhinMasterCSV selectedMaster;
	private String validFrom;

	public AddIyakuhinMenu(Menu parentMenu, MenuExecEnv env){
		this.parentMenu = parentMenu;
		Path masterFilesDirectory = env.getMasterFilesDirectory();
		this.zipFilePath = MasterZipFinder.findMasterZip(masterFilesDirectory, MasterZipFinder.IYAKUHIN_PREFIX);
		selections = new ArrayList<>();
		validFrom = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	@Override
	public String getPrompt(){
		return "add-iyakuhin>";
	}

	@Override
	public void printMessage(MenuExecEnv env){
		env.out.printf("zipfile: %s\n", zipFilePath == null ? "(none)" : zipFilePath);
		if( selectedMaster != null ){
			env.out.printf("selected: %s (%d)\n", selectedMaster.name, selectedMaster.iyakuhincode);
		} else {
			env.out.printf("selected: (none)\n");
		}
		env.out.printf("valid-from: %s\n", validFrom);
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("search",
			"searchs texts in zip file",
			new String[]{
				"syntax: search text...",
				"  text - space separated texts to search",
				"         Entries that contain each text in that order are searched."
			},
			(arg, env) -> {
				try{
					String[] texts = arg.split("(?U:\\s)");
					List<CSVRecord> records = CSVSearcher.searchCSV(texts, zipFilePath, MasterZipFinder.IYAKUHIN_PREFIX + ".csv", 5);
					selections.clear();
					records.stream().forEach(rec -> {
						CommonsCSVRow row = new CommonsCSVRow(rec);
						IyakuhinMasterCSV master = new IyakuhinMasterCSV(row);
						selections.add(master);
					});
					printSelections(env);
				} catch(IOException ex){
					ex.printStackTrace(env.out);
				}
				return this;
			}
		));
		commands.add(Command.create("select",
			"selects from search result",
			new String[]{
				"syntax: select index",
				"  index - 1-based index"
			},
			(arg, env) -> {
				try {
					int index = Integer.parseInt(arg);
					selectedMaster = selections.get(index-1);
				} catch(NumberFormatException ex){
					env.out.println("invalid number format");
				} catch(IndexOutOfBoundsException ex){
					env.out.println("index is out of range");
				}
				return this;
			}
		));
		commands.add(Command.create("enter-new",
			"enters the currently selected record as a new master record",
			"syntax: enter-new",
			(arg, env) -> {
				if( confirmEnterNew(env) ){
					enterNew(selectedMaster, validFrom, env);
					selectedMaster = null;
					env.out.println("enter-new done");
				} else {
					env.out.println("enter-new aborted");
				}
				return this;
			}
		));
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			(arg, env) -> parentMenu
		));
		return commands;
	}

	private void printSelections(MenuExecEnv env){
		for(int i=0;i<selections.size();i++){
			IyakuhinMasterCSV master = selections.get(i);
			env.out.printf("%2d.%s (%d)\n", i+1, master.name, master.iyakuhincode);
		}
	}

	private boolean confirmEnterNew(MenuExecEnv env){
		if( selectedMaster == null ){
			env.out.println("No master record is selected!");
			return false;
		}
		int iyakuhincode = selectedMaster.iyakuhincode;
		JdbcTemplate jdbcTemplate = env.getJdbcTemplate();
		String sql = "select count(*) from iyakuhin_master_arch where " + 
			" iyakuhincode = ? and valid_from <= ? and " + 
			" (valid_upto = '0000-00-00' or ? <= valid_upto) ";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
			iyakuhincode, validFrom, validFrom);
		if( count > 0 ){
			env.out.println("Master record is already available!");
			return false;
		}
		return confirmMasterDataForEnter(env, "Is it OK to enter the above master data as new record?");
	}

	private boolean confirmMasterDataForEnter(MenuExecEnv env, String prompt){
		IyakuhinMasterCSV master = selectedMaster;
		env.out.println("iyakuhincode: " + master.iyakuhincode);
		env.out.println("yakkacode: " + master.yakkacode);
		env.out.println("name: " + master.name);
		env.out.println("yomi: " + master.yomi);
		env.out.println("unit: " + master.unit);
		env.out.println("yakka: " + master.yakka);
		env.out.println("madoku: " + master.madoku);
		env.out.println("kouhatsu: " + master.kouhatsu);
		env.out.println("zaikei: " + master.zaikei);
		env.out.println("valid_from: " + validFrom);
		env.out.println("valid_upto: " + "0000-00-00");
		boolean ok = false;
		while( true ){
			String input = env.readLine(prompt + " (Y/N) >>");
			if( input.length() > 0 && input.charAt(0) == 'Y' ){
				ok = true;
				break;
			} else if( input.length() > 0 && input.charAt(0) == 'N' ){
				ok = false;
				break;
			}
			env.out.println("Invalid input. Please input Y or N.");
		}
		return ok;
	}

	private void enterNew(IyakuhinMasterCSV master, String validFrom, MenuExecEnv env){
		String sql = "insert into iyakuhin_master_arch set " +
			" iyakuhincode = ?, " + 
			" yakkacode = ?, " + 
			" name = ?, " + 
			" yomi = ?, " + 
			" unit = ?, " + 
			" yakka = ?, " + 
			" madoku = ?, " + 
			" kouhatsu = ?, " + 
			" zaikei = ?, " + 
			" valid_from = ?, " + 
			" valid_upto = ?"; 
		JdbcTemplate jdbcTemplate = env.getJdbcTemplate();
		int nAffected = jdbcTemplate.update(sql,
			master.iyakuhincode,
			master.yakkacode,
			master.name,
			master.yomi,
			master.unit,
			master.yakka,
			master.madoku,
			master.kouhatsu,
			master.zaikei,
			validFrom,
			"0000-00-00"
		);
		if( nAffected != 1 ){
			env.out.println("ERROR: insert failed");
		}
	}

}
