package jp.chang.myclinic.masterapp;

import java.util.List;
import java.util.Collections;

public interface Menu {
	default void printMessage(MenuExecEnv env){

	}

	default String getPrompt(){
		return ">";
	}

	default List<Command> getCommands(){
		return Collections.emptyList();
	}
}