package jp.chang.myclinic.masterapp;

import jp.chang.myclinic.master.download.MasterDownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class DownloadMenu implements Menu {

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
		commands.add(Command.create("download-shinryou",
			"downloads shinryou master file",
			"syntax: download-shinryou",
			this::downloadShinryou
		));
		commands.add(Command.create("download-kizai",
			"downloads kizai master file",
			"syntax: download-kizai",
			this::downloadKizai
		));
		commands.add(Command.create("download-shoubyoumei",
			"downloads shoubyoumei master file",
			"syntax: download-shoubyoumei",
			this::downloadShoubyoumei
		));
		commands.add(Command.create("download-shuushokugo",
			"downloads shuushokugo master file",
			"syntax: download-shuushokugo",
			this::downloadShuushokugo
		));
		commands.add(Command.create("download-all",
			"downloads all master files",
			"syntax: download-all",
			this::downloadAll
		));
		commands.add(Command.create("return",
			"returns to parent menu",
			"syntax: return",
			(arg, env) -> parentMenu
		));
		return commands;
	}

	private Menu downloadIyakuhin(String arg, MenuExecEnv env){
		MasterDownloader downloader = new MasterDownloader();
		String ts = makeTimeStamp();
		String name = "y-" + ts + ".zip";
		Path dir = env.getMasterFilesDirectory();
		Path file = dir.resolve(Paths.get(name));
		try { 
			downloader.downloadIyakuhin(file);
			env.out.printf("Downloaded iyakuhin master as %s.\n", file);
		} catch(IOException ex){
			ex.printStackTrace(env.out);
			env.out.println("Failed to download iyakuhin master.");
		}
		return this;
	}

	private Menu downloadShinryou(String arg, MenuExecEnv env){
		MasterDownloader downloader = new MasterDownloader();
		String ts = makeTimeStamp();
		String name = "s-" + ts + ".zip";
		Path dir = env.getMasterFilesDirectory();
		Path file = dir.resolve(Paths.get(name));
		try {
			downloader.downloadShinryou(file);
			env.out.printf("Downloaded shinryou master as %s.\n", file);
		} catch(IOException ex){
			ex.printStackTrace(env.out);
			env.out.println("Failed to download shinryou master.");
		}
		return this;
	}

	private Menu downloadKizai(String arg, MenuExecEnv env){
		MasterDownloader downloader = new MasterDownloader();
		String ts = makeTimeStamp();
		String name = "t-" + ts + ".zip";
		Path dir = env.getMasterFilesDirectory();
		Path file = dir.resolve(Paths.get(name));
		try {
			downloader.downloadKizai(file);
			env.out.printf("Downloaded kizai master as %s.\n", file);
		} catch(IOException ex){
			ex.printStackTrace(env.out);
			env.out.println("Failed to download kizai master.");
		}
		return this;
	}

	private Menu downloadShoubyoumei(String arg, MenuExecEnv env){
		MasterDownloader downloader = new MasterDownloader();
		String ts = makeTimeStamp();
		String name = "b-" + ts + ".zip";
		Path dir = env.getMasterFilesDirectory();
		Path file = dir.resolve(Paths.get(name));
		try {
			downloader.downloadShoubyoumei(file);
			env.out.printf("Downloaded shoubyoumei master as %s.\n", file);
		} catch(IOException ex){
			ex.printStackTrace(env.out);
			env.out.println("Failed to download shoubyoumei master.");
		}
		return this;
	}

	private Menu downloadShuushokugo(String arg, MenuExecEnv env){
		MasterDownloader downloader = new MasterDownloader();
		String ts = makeTimeStamp();
		String name = "z-" + ts + ".zip";
		Path dir = env.getMasterFilesDirectory();
		Path file = dir.resolve(Paths.get(name));
		try {
			downloader.downloadShuushokugo(file);
			env.out.printf("Downloaded shuushokugo master as %s.\n", file);
		} catch(IOException ex){
			ex.printStackTrace(env.out);
			env.out.println("Failed to download shuushokugo master.");
		}
		return this;
	}

	private Menu downloadAll(String arg, MenuExecEnv env){
		MasterDownloader downloader = new MasterDownloader();
		String ts = makeTimeStamp();
		{
			String name = "y-" + ts + ".zip";
			Path dir = env.getMasterFilesDirectory();
			Path file = dir.resolve(Paths.get(name));
			try {
				downloader.downloadIyakuhin(file);
				env.out.printf("Downloaded iyakuhin master as %s.\n", file);
			} catch(IOException ex){
				ex.printStackTrace(env.out);
				env.out.println("Failed to download iyakuhin master.");
			}
		}
		{
			String name = "s-" + ts + ".zip";
			Path dir = env.getMasterFilesDirectory();
			Path file = dir.resolve(Paths.get(name));
			try {
				downloader.downloadShinryou(file);
				env.out.printf("Downloaded shinryou master as %s.\n", file);
			} catch(IOException ex){
				ex.printStackTrace(env.out);
				env.out.println("Failed to download shinryou master.");
			}
		}
		{
			String name = "t-" + ts + ".zip";
			Path dir = env.getMasterFilesDirectory();
			Path file = dir.resolve(Paths.get(name));
			try {
				downloader.downloadKizai(file);
				env.out.printf("Downloaded kizai master as %s.\n", file);
			} catch(IOException ex){
				ex.printStackTrace(env.out);
				env.out.println("Failed to download kizai master.");
			}
		}
		{
			String name = "b-" + ts + ".zip";
			Path dir = env.getMasterFilesDirectory();
			Path file = dir.resolve(Paths.get(name));
			try {
				downloader.downloadShoubyoumei(file);
				env.out.printf("Downloaded shoubyoumei master as %s.\n", file);
			} catch(IOException ex){
				ex.printStackTrace(env.out);
				env.out.println("Failed to download shoubyoumei master.");
			}
		}
		{
			String name = "z-" + ts + ".zip";
			Path dir = env.getMasterFilesDirectory();
			Path file = dir.resolve(Paths.get(name));
			try {
				downloader.downloadShuushokugo(file);
				env.out.printf("Downloaded shuushokugo master as %s.\n", file);
			} catch(IOException ex){
				ex.printStackTrace(env.out);
				env.out.println("Failed to download shuushokugo master.");
			}
		}
		return this;
	}

	private String makeTimeStamp(){
		LocalDateTime tm = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");
		return tm.format(formatter);
	}

}