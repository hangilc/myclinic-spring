package jp.chang.myclinic.dto;

public class ShahokokuhoDTO {
	public int shahokokuhoId;
	public int patientId;
	public int hokenshaBangou;
	public String hihokenshaKigou;
	public String hihokenshaBangou;
	public int honnin;
	public int kourei;
	public String validFrom;
	public String validUpto;

	public static ShahokokuhoDTO copy(ShahokokuhoDTO src){
		ShahokokuhoDTO dst = new ShahokokuhoDTO();
		dst.shahokokuhoId = src.shahokokuhoId;
		dst.patientId = src.patientId;
		dst.hokenshaBangou = src.hokenshaBangou;
		dst.hihokenshaKigou = src.hihokenshaKigou;
		dst.hihokenshaBangou = src.hihokenshaBangou;
		dst.honnin = src.honnin;
		dst.kourei = src.kourei;
		dst.validFrom = src.validFrom;
		dst.validUpto = src.validUpto;
		return dst;
	}

	@Override
	public String toString(){
		return "ShahokokuhoDTO[" +
			"shahokokuhoId=" + shahokokuhoId + "," +
			"patientId=" + patientId + "," +
			"hokenshaBangou=" + hokenshaBangou + "," +
			"hihokenshaKigou=" + hihokenshaKigou + "," +
			"hihokenshaBangou=" + hihokenshaBangou + "," +
			"honnin=" + honnin + "," +
			"kourei=" + kourei + "," +
			"validFrom=" + validFrom + "," +
			"validUpto=" + validUpto +
		"]";
	}
}