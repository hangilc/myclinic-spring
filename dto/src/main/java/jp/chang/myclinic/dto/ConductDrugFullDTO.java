package jp.chang.myclinic.dto;

public class ConductDrugFullDTO {
	public ConductDrugDTO conductDrug;
	public IyakuhinMasterDTO master;

	public static ConductDrugFullDTO copy(ConductDrugFullDTO src){
		ConductDrugFullDTO dst = new ConductDrugFullDTO();
		dst.conductDrug = ConductDrugDTO.copy(src.conductDrug);
		dst.master = src.master;
		return dst;
	}
}