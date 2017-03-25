package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
		String ts = makeTimeStamp();
		String name = "y-" + ts + ".zip";
		Path dir = Globals.getMasterFilesDirectory();
		Path file = dir.resolve(Paths.get(name));
		try { 
			downloader.downloadIyakuhin(file);
			System.out.printf("Downloaded iyakuhin master as %s.\n", file);
		} catch(IOException ex){
			ex.printStackTrace(System.out);
			System.out.println("Failed to download iyakuhin master.");
		}
		return this;
	}

	private String makeTimeStamp(){
		LocalDateTime tm = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");
		return tm.format(formatter);
	}

}