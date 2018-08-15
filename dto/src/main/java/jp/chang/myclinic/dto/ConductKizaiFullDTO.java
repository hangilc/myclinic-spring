package jp.chang.myclinic.dto;

public class ConductKizaiFullDTO {
	public ConductKizaiDTO conductKizai;
	public KizaiMasterDTO master;

	public static ConductKizaiFullDTO copy(ConductKizaiFullDTO src){
		ConductKizaiFullDTO dst = new ConductKizaiFullDTO();
		dst.conductKizai = ConductKizaiDTO.copy(src.conductKizai);
		dst.master = src.master;
		return dst;
	}
}