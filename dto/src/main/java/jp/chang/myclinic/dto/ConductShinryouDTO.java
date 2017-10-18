package jp.chang.myclinic.dto;

public class ConductShinryouDTO {
	public int conductShinryouId;
	public int conductId;
	public int shinryoucode;

	public static ConductShinryouDTO copy(ConductShinryouDTO src){
		ConductShinryouDTO dst = new ConductShinryouDTO();
		dst.conductShinryouId = src.conductShinryouId;
		dst.conductId = src.conductId;
		dst.shinryoucode = src.shinryoucode;
		return dst;
	}
	
	@Override
	public String toString(){
		return "ConductShinryouDTO[" +
			"conductShinryouId=" + conductShinryouId + ", " +
			"conductId=" + conductId + ", " +
			"shinryoucode=" + shinryoucode +
		"]";
	}
}
