package jp.chang.myclinic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import jp.chang.myclinic.master.MasterZipFinder;

class IyakuhinAddMenu extends Menu {

	private Path zipFilePath;

	IyakuhinAddMenu(){
		Path masterDir = Paths.get(".");
		zipFilePath = MasterZipFinder.findMasterZip(masterDir, MasterZipFinder.IYAKUHIN_PREFIX);
		System.out.println(zipFilePath);
	}

	@Override
	public String getPrompt(){
		return "iyakuhin-add>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();

		return commands;
	}
}