package jp.chang.myclinic.consts;

public enum Sex {
	Male("M", "男"),
	Female("F", "女");

	private final String code;
	private final String kanji;

	Sex(String code, String kanji){
		this.code = code;
		this.kanji = kanji;
	}

	public String getCode(){
		return code;
	}

	public String getKanji(){
		return kanji;
	}

	public static Sex fromCode(String code){
		if( code != null ){
			if( code.equals("M") ){
				return Male;
			}
			if( code.equals("F") ){
				return Female;
			}
		}
		return null;
	}
}