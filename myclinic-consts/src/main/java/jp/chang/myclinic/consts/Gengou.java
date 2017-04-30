package jp.chang.myclinic.consts;

import java.time.chrono.JapaneseEra;

public enum Gengou {
	Meiji(JapaneseEra.MEIJI, "明治"),
	Taishou(JapaneseEra.TAISHO, "大正"),
	Shouwa(JapaneseEra.SHOWA, "昭和"),
	Heisei(JapaneseEra.HEISEI, "平成");

	private JapaneseEra code;
	private String kanji;

	Gengou(JapaneseEra code, String kanji){
		this.code = code;
		this.kanji = kanji;
	}

	public JapaneseEra getCode(){
		return code;
	}

	public String getKanji(){
		return kanji;
	}

	public static Gengou fromKanji(String kanji){
		for(Gengou gengou: values()){
			if( gengou.kanji.equals(kanji) ){
				return gengou;
			}
		}
		return null;
	}

	public static Gengou fromEra(JapaneseEra era){
		for(Gengou gengou: values()){
			if( gengou.code == era ){
				return gengou;
			}
		}
		return null;
	}
}