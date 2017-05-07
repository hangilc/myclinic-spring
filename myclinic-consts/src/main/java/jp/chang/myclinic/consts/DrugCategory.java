package jp.chang.myclinic.consts;

public enum DrugCategory {
	Naifuku(MyclinicConsts.DrugCategoryNaifuku),
	Tonpuku(MyclinicConsts.DrugCategoryTonpuku),
	Gaiyou(MyclinicConsts.DrugCategoryGaiyou);

	private int code;

	DrugCategory(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public static DrugCategory fromCode(int code){
		for(DrugCategory c: values()){
			if( c.code == code ){
				return c;
			}
		}
		return null;
	}
}