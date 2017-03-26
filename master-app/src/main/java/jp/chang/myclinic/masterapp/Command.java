package jp.chang.myclinic.masterapp;

interface Command {

	interface CommandExecutor {
		Menu execute(String arg, MenuExecEnv env);
	}

	String getName();
	String getDescription();
	String getDetail();
	Menu exec(String arg, MenuExecEnv env);

	static Command create(String name, String description, String[] detailLines, CommandExecutor executor){
		return new Command(){
			@Override
			public String getName(){
				return name;
			}

			@Override
			public String getDescription(){
				return description;
			}

			@Override
			public String getDetail(){
				return String.join("\n", detailLines);
			}

			@Override
			public Menu exec(String arg, MenuExecEnv env){
				return executor.execute(arg, env);
			}
		};
	}

	static Command create(String name, String description, String detail, CommandExecutor executor){
		return create(name, description, new String[]{ detail }, executor);
	}
}