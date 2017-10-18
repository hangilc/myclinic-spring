package jp.chang.myclinic.dto;

public class DrugFullDTO {
	public DrugDTO drug;
	public IyakuhinMasterDTO master;

	public static DrugFullDTO copy(DrugFullDTO src){
		DrugFullDTO dst = new DrugFullDTO();
		dst.drug = src.drug;
		dst.master = src.master;
		return dst;
	}
}