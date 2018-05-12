package jp.chang.myclinic.consts;

public enum DrugCategory {
	Naifuku(MyclinicConsts.DrugCategoryNaifuku, "内服"),
	Tonpuku(MyclinicConsts.DrugCategoryTonpuku, "頓服"),
	Gaiyou(MyclinicConsts.DrugCategoryGaiyou, "外用"),
	Injection(MyclinicConsts.DrugCategoryInjection, "注射");

	private int code;
	private String kanji;

	DrugCategory(int code, String kanji){
		this.code = code;
		this.kanji = kanji;
	}

	public int getCode(){
		return code;
	}

	public String getKanji(){
		return kanji;
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