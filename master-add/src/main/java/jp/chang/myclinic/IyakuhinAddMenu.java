package jp.chang.myclinic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.csv.CSVRecord;
import jp.chang.myclinic.master.MasterZipFinder;
import jp.chang.myclinic.master.CSVSearcher;
import jp.chang.myclinic.master.IyakuhinMasterCSV;
import jp.chang.myclinic.master.CommonsCSVRow;
import org.springframework.jdbc.core.JdbcTemplate;

class IyakuhinAddMenu extends Menu {

	private Menu parentMenu;
	private Path zipFilePath;
	private List<IyakuhinMasterCSV> selections;
	private IyakuhinMasterCSV selectedMaster;
	private String validFrom;

	IyakuhinAddMenu(Menu parentMenu){
		this.parentMenu = parentMenu;
		Path masterDir = Paths.get(".");
		zipFilePath = MasterZipFinder.findMasterZip(masterDir, MasterZipFinder.IYAKUHIN_PREFIX);
		selections = new ArrayList<>();
		validFrom = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	@Override
	public String getPrompt(){
		return "iyakuhin-add>";
	}

	@Override
	public void printMessage(){
		System.out.printf("zipfile: %s\n", zipFilePath == null ? "(none)" : zipFilePath);
		if( selectedMaster != null ){
			System.out.printf("selected: %s (%d)\n", selectedMaster.name, selectedMaster.iyakuhincode);
		} else {
			System.out.printf("selected: (none)\n");
		}
		System.out.printf("valid-from: %s\n", validFrom);
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
			arg -> {
				try{
					String[] texts = arg.split("(?U:\\s)");
					List<CSVRecord> records = CSVSearcher.searchCSV(texts, zipFilePath, MasterZipFinder.IYAKUHIN_PREFIX + ".csv", 5);
					selections.clear();
					records.stream().forEach(rec -> {
						CommonsCSVRow row = new CommonsCSVRow(rec);
						IyakuhinMasterCSV master = new IyakuhinMasterCSV(row);
						selections.add(master);
					});
					printSelections();
				} catch(IOException ex){
					ex.printStackTrace(System.out);
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
			arg -> {
				try {
					int index = Integer.parseInt(arg);
					selectedMaster = selections.get(index-1);
				} catch(NumberFormatException ex){
					System.out.println("invalid number format");
				} catch(IndexOutOfBoundsException ex){
					System.out.println("index is out of range");
				}
				return this;
			}
		));
		commands.add(Command.create("enter-new",
			"enters the currently selected record as a new master record",
			"syntax: enter-new",
			arg -> {
				if( confirmEnterNew() ){
					enterNew(selectedMaster, validFrom);
					selectedMaster = null;
					System.out.println("enter-new done");
				} else {
					System.out.println("enter-new aborted");
				}
				return this;
			}
		));
		commands.add(Command.create("return", 
			"returns to the parent menu", 
			"syntax: return", 
			arg -> parentMenu
			));
		return commands;
	}

	private void printSelections(){
		for(int i=0;i<selections.size();i++){
			IyakuhinMasterCSV master = selections.get(i);
			System.out.printf("%2d.%s (%d)\n", i+1, master.name, master.iyakuhincode);
		}
	}

	private boolean confirmEnterNew(){
		if( selectedMaster == null ){
			System.out.println("No master record is selected!");
			return false;
		}
		int iyakuhincode = selectedMaster.iyakuhincode;
		JdbcTemplate jdbcTemplate = JdbcUtil.jdbcTemplate;
		String sql = "select count(*) from iyakuhin_master_arch where " + 
			" iyakuhincode = ? and valid_from <= ? and " + 
			" (valid_upto = '0000-00-00' or ? <= valid_upto) ";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
			iyakuhincode, validFrom, validFrom);
		if( count > 0 ){
			System.out.println("Master record is already available!");
			return false;
		}
		return confirmMasterDataForEnter("Is it OK to enter the above master data as new record?");
	}

	private boolean confirmMasterDataForEnter(String prompt){
		IyakuhinMasterCSV master = selectedMaster;
		System.out.println("currently selected master is\n");
		System.out.println("iyakuhincode: " + master.iyakuhincode);
		System.out.println("yakkacode: " + master.yakkacode);
		System.out.println("name: " + master.name);
		System.out.println("yomi: " + master.yomi);
		System.out.println("unit: " + master.unit);
		System.out.println("yakka: " + master.yakka);
		System.out.println("madoku: " + master.madoku);
		System.out.println("kouhatsu: " + master.kouhatsu);
		System.out.println("zaikei: " + master.zaikei);
		System.out.println("valid_from: " + validFrom);
		System.out.println("valid_upto: " + "0000-00-00");
		System.out.println(prompt);
		boolean ok = false;
		while( true ){
			System.out.print("Input Yes or No: ");
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			if( input.length() > 0 && input.charAt(0) == 'Y' ){
				ok = true;
				break;
			} else if( input.length() > 0 && input.charAt(0) == 'N' ){
				ok = false;
				break;
			}
		}
		return ok;
	}

	private static void enterNew(IyakuhinMasterCSV master, String validFrom){
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
		JdbcTemplate jdbcTemplate = JdbcUtil.jdbcTemplate;
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
			throw new RuntimeException("insert failed");
		}
	}

}