package jp.chang.myclinic.consts;

public enum HoukatsuKensaCode {
	NONE("00"),
	KETSUEKIKAGAKU("01"),
	ENDOCRINE("02"),
	HEPATITIS("03"),
	TUMORMARKER("05"),
	COAGULO("06"),
	AUTOANTIBODY("07"),
	TOLERANCE("08"),
	ANTIBODY("09"),
	GLOBULINCLASS("10"),
	IGE("11");

	private String label;

	HoukatsuKensaCode(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}
}