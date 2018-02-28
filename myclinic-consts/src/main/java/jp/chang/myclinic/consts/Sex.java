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
		for(Sex sex: values()){
			if( sex.getCode().equals(code) ){
				return sex;
			}
		}
		return null;
	}

	public static Sex fromKanji(String kanji){
		for(Sex sex: values()){
			if( sex.getKanji().equals(kanji) ){
				return sex;
			}
		}
		return null;
	}

	public static String codeToKanji(String code){
		Sex sex = fromCode(code);
		if( sex == null ){
			return "??";
		} else {
			return sex.getKanji();
		}
	}

}