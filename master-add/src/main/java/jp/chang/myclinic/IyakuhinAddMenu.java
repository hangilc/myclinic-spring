package jp.chang.myclinic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import org.apache.commons.csv.CSVRecord;
import jp.chang.myclinic.master.MasterZipFinder;
import jp.chang.myclinic.master.CSVSearcher;
import jp.chang.myclinic.master.IyakuhinMasterCSV;
import jp.chang.myclinic.master.CommonsCSVRow;

class IyakuhinAddMenu extends Menu {

	private Menu parentMenu;
	private Path zipFilePath;
	private List<IyakuhinMasterCSV> selections;

	IyakuhinAddMenu(Menu parentMenu){
		this.parentMenu = parentMenu;
		Path masterDir = Paths.get(".");
		zipFilePath = MasterZipFinder.findMasterZip(masterDir, MasterZipFinder.IYAKUHIN_PREFIX);
		selections = new ArrayList<>();
		System.out.println(zipFilePath);
	}

	@Override
	public String getPrompt(){
		return "iyakuhin-add>";
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

}