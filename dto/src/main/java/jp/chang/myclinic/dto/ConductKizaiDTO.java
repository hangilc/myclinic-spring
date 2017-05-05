package jp.chang.myclinic.dto;

public class ConductKizaiDTO {
	public int conductKizaiId;
	public int conductId;
	public int kizaicode;
	public double amount;
	
	@Override
	public String toString(){
		return "ConductKizaiDTO[" +
			"conductKizaiId=" + conductKizaiId + ", " +
			"conductId=" + conductId + ", " +
			"kizaicode=" + kizaicode + ", " +
			"amount=" + amount + 
		"]";
	}
}