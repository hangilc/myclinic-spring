package jp.chang.myclinic.dto;

public class ShinryouFullDTO {
	public ShinryouDTO shinryou;
	public ShinryouMasterDTO master;

	public static ShinryouFullDTO create(ShinryouDTO shinryou, ShinryouMasterDTO master){
		ShinryouFullDTO result = new ShinryouFullDTO();
		result.shinryou = shinryou;
		result.master = master;
		return result;
	}
}