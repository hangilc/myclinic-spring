package jp.chang.myclinic.dto;

public class ConductDrugDTO {
	public int conductDrugId;
	public int conductId;
	public int iyakuhincode;
	public double amount;
	
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