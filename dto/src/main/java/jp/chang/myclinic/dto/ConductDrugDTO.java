package jp.chang.myclinic.dto;

public class ConductDrugDTO {
	public int conductDrugId;
	public int conductId;
	public int iyakuhincode;
	public double amount;

	public static ConductDrugDTO copy(ConductDrugDTO src){
		ConductDrugDTO dst = new ConductDrugDTO();
		dst.conductDrugId = src.conductDrugId;
		dst.conductId = src.conductId;
		dst.iyakuhincode = src.iyakuhincode;
		dst.amount = src.amount;
		return dst;
	}
	
	@Override
	public String toString(){
		return "ConductDrugDTO[" +
			"conductDrugId=" + conductDrugId + ", " +
			"conductId=" + conductId + ", " +
			"iyakuhincode=" + iyakuhincode + ", " +
			"amount=" + amount + 
		"]";
	}
}