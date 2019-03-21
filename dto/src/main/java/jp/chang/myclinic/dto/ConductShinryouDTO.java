package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

public class ConductShinryouDTO {
	@Primary
	@AutoInc
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
