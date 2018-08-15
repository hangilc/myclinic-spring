package jp.chang.myclinic.dto;

public class ConductShinryouFullDTO {
	public ConductShinryouDTO conductShinryou;
	public ShinryouMasterDTO master;

	public static ConductShinryouFullDTO copy(ConductShinryouFullDTO src){
		ConductShinryouFullDTO dst = new ConductShinryouFullDTO();
		dst.conductShinryou = ConductShinryouDTO.copy(src.conductShinryou);
		dst.master = src.master; // master is considered to be immutable
		return dst;
	}
}