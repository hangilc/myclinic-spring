package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import jp.chang.myclinic.master.MasterDownloader;

class DownloadMenu extends Menu {

	private Menu parentMenu;

	DownloadMenu(Menu parentMenu){
		this.parentMenu = parentMenu;
	}

	@Override
	public String getPrompt(){
		return "download>";
	}

	@Override
	public List<Command> getCommands(){
		List<Command> commands = new ArrayList<>();
		commands.add(Command.create("download-iyakuhin",
			"downloads iyakuhin master file",
			"syntax: download-iyakuhin",
			this::downloadIyakuhin
		));
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			arg -> parentMenu
		));
		return commands;
	}

	private Menu downloadIyakuhin(String arg){
		MasterDownloader downloader = new MasterDownloader();
		String name = "y.zip";
		Path dir = Globals.getMasterFilesDirectory();
		Path file = dir.resolve(Paths.get(name));
		try { 
			downloader.downloadIyakuhin(file);
		} catch(IOException ex){
			ex.printStackTrace(System.out);
			System.out.println("Failed to download iyakuhin master.");
		}
		return this;
	}

}