package jp.chang.myclinic.mastermanip.lib;

public interface CSVRow {
	String getString(int index);
	default int getInt(int index) throws NumberFormatException {
		return Integer.parseInt(getString(index));
	}
}