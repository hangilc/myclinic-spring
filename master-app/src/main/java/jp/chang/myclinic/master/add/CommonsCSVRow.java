package jp.chang.myclinic.master.add;

import org.apache.commons.csv.CSVRecord;

public class CommonsCSVRow implements CSVRow {
	private CSVRecord record;

	public CommonsCSVRow(CSVRecord record){
		this.record = record;
	}

	public String getString(int index){
		return this.record.get(index-1);
	}

}