package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

public class ConductDTO {
	@Primary @AutoInc
	public int conductId;
	public int visitId;
	public int kind;

	public static ConductDTO copy(ConductDTO src){
		ConductDTO dst = new ConductDTO();
		dst.conductId = src.conductId;
		dst.visitId = src.visitId;
		dst.kind = src.kind;
		return dst;
	}

	@Override
	public String toString(){
		return "ConductDTO[" +
		"conductId=" + conductId + "," +
		"visitId=" + visitId + "," +
		"kind=" + kind + //"," +
		"]";
	}
}