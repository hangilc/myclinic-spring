package jp.chang.myclinic;

interface Command {
	String getName();
	Menu exec(String arg);
	String getDescription();
	String getDetail();
}