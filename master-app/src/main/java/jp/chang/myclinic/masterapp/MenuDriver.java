package jp.chang.myclinic.masterapp;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Component
public class MenuDriver {

	private MenuExecEnv env;

	@Autowired
	public void setMenuExecEnv(MenuExecEnv env){
		this.env = env;
	}

	public MenuExecEnv getMenuExecEnv(){
		return env;
	}

	public void run(Menu menu){
		while( menu != null ){
			menu.printMessage(env);
			env.out.println("(Type 'help' for help.)");
			String cmdline = env.readLine(menu.getPrompt());
			menu = doAction(menu, cmdline);
		}
	}

	private Menu doAction(Menu menu, String cmdline){
		cmdline = cmdline.trim();
		if( "".equals(cmdline) ){
			return menu;
		}
		String[] parts = cmdline.split("\\s", 2);
		return doCommand(menu, parts[0], parts.length > 1 ? parts[1] : "");
	}

	private Menu doCommand(Menu menu, String cmd, String arg){
		if( "help".equals(cmd) ){
			if( arg == null || arg.isEmpty() ){
				doHelp(menu);
			} else {
				doHelp(menu, arg);
			}
			return menu;
		}
		List<Command> commands = menu.getCommands();
		for(int i=0;i<commands.size();i++){
			if( cmd.equals(commands.get(i).getName()) ){
				return commands.get(i).exec(arg, env);
			}
		}
		if( "abort".equals(cmd) ){
			return null;
		}
		System.out.println("unknown command: " + cmd);
		return menu;
	}

	private void doHelp(Menu menu){
		menu.getCommands().stream().forEach(c -> {
			env.out.println(c.getName() + " - " + c.getDescription());
		});
		env.out.println("help - prints help");
	}

	private void doHelp(Menu menu, String cmd){
		if( "help".equals(cmd) ){
			env.out.println("help - prints help");
			env.out.println("syntax: help");
			env.out.println("  prints available commands");
			env.out.println("syntax: help command");
			env.out.println("  prints the detailed help of the command");
			return;
		}
		List<Command> commands = menu.getCommands();
		for(int i=0;i<commands.size();i++){
			Command c = commands.get(i);
			if( cmd.equals(c.getName()) ){
				env.out.println(c.getName() + " - " + c.getDescription());
				env.out.println(c.getDetail());
				return;
			}
		}
		env.out.println("unknown command: " + cmd);
	}

}