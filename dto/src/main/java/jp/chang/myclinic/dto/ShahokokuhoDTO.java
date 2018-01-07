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

	public ShahokokuhoDTO copy(){
		ShahokokuhoDTO dst = new ShahokokuhoDTO();
		dst.shahokokuhoId = shahokokuhoId;
		dst.patientId = patientId;
		dst.hokenshaBangou = hokenshaBangou;
		dst.hihokenshaKigou = hihokenshaKigou;
		dst.hihokenshaBangou = hihokenshaBangou;
		dst.honnin = honnin;
		dst.kourei = kourei;
		dst.validFrom = validFrom;
		dst.validUpto = validUpto;
		return dst;
	}

	public void assign(ShahokokuhoDTO src){
		shahokokuhoId = src.shahokokuhoId;
		patientId = src.patientId;
		hokenshaBangou = src.hokenshaBangou;
		hihokenshaKigou = src.hihokenshaKigou;
		hihokenshaBangou = src.hihokenshaBangou;
		honnin = src.honnin;
		kourei = src.kourei;
		validFrom = src.validFrom;
		validUpto = src.validUpto;
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