package jp.chang.myclinic;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.List;

abstract class Menu {

	static protected BufferedReader reader;

	static {
		InputStreamReader r = new InputStreamReader(System.in);
		reader = new BufferedReader(r);
	}

	public Menu exec() throws IOException {
		printMessage();
		System.out.println("(Type 'help' for help.)");
		System.out.print(getPrompt());
		String cmdline = reader.readLine();
		return doAction(cmdline);
	}

	protected void printMessage(){
		return;
	}

	protected String getPrompt(){
		return ">";
	}

	private Menu doAction(String cmdline){
		cmdline = cmdline.trim();
		if( "".equals(cmdline) ){
			return this;
		}
		String[] parts = cmdline.split("\\s", 2);
		return doCommand(parts[0], parts.length > 1 ? parts[1] : "");
	}

	abstract List<Command> getCommands();

	protected Menu doCommand(String cmd, String arg){
		if( "help".equals(cmd) ){
			if( arg == null || arg.isEmpty() ){
				doHelp();
			} else {
				doHelp(arg);
			}
			return this;
		}
		List<Command> commands = getCommands();
		for(int i=0;i<commands.size();i++){
			if( cmd.equals(commands.get(i).getName()) ){
				return commands.get(i).exec(arg);
			}
		}
		System.out.println("unknown command: " + cmd);
		return this;
	}

	private void doHelp(){
		getCommands().stream().forEach(c -> {
			System.out.println(c.getName() + " - " + c.getDescription());
		});
		System.out.println("help - prints help");
	}

	private void doHelp(String cmd){
		if( "help".equals(cmd) ){
			System.out.println("help - prints help");
			System.out.println("syntax: help");
			System.out.println("  prints available commands");
			System.out.println("syntax: help command");
			System.out.println("  prints the detailed help of the command");
			return;
		}
		List<Command> commands = getCommands();
		for(int i=0;i<commands.size();i++){
			Command c = commands.get(i);
			if( cmd.equals(c.getName()) ){
				System.out.println(c.getName() + " - " + c.getDescription());
				System.out.println(c.getDetail());
				return;
			}
		}
		System.out.println("unknown command: " + cmd);
	}
}