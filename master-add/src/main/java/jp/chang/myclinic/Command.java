package jp.chang.myclinic;

interface Command {
	String getName();
	String getDescription();
	String getDetail();
	Menu exec(String arg);
}