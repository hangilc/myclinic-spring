package jp.chang.myclinic.dto;

public class ConductShinryouDTO {
	public int conductShinryouId;
	public int conductId;
	public int shinryoucode;
	
	@Override
	public String toString(){
		return "ConductShinryouDTO[" +
			"conductShinryouId=" + conductShinryouId + ", " +
			"conductId=" + conductId + ", " +
			"shinryoucode=" + shinryoucode +
		"]";
	}
}
