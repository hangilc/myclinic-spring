package jp.chang.myclinic.consts;

import java.time.chrono.JapaneseEra;
import java.util.ArrayList;
import java.util.List;

public enum Gengou {
	Meiji(JapaneseEra.MEIJI, "明治", "meiji"),
	Taishou(JapaneseEra.TAISHO, "大正", "taishou"),
	Shouwa(JapaneseEra.SHOWA, "昭和", "shouwa"),
	Heisei(JapaneseEra.HEISEI, "平成", "heisei");

	private JapaneseEra era;
	private String kanji;
	private String romaji;

	Gengou(JapaneseEra era, String kanji, String romaji){
		this.era = era;
		this.kanji = kanji;
	}

	public JapaneseEra getEra(){
		return era;
	}

	public String getKanji(){
		return kanji;
	}

	public String getRomaji(){
		return romaji;
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
			if( gengou.era == era ){
				return gengou;
			}
		}
		return null;
	}

	public static Gengou Current = Heisei;

	public static List<Gengou> Recent = new ArrayList<Gengou>();
	static {
		Recent.add(Heisei);
	}

	public static Gengou MostRecent = Heisei;

}