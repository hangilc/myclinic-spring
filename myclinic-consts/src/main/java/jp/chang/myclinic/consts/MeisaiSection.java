package jp.chang.myclinic.consts;

public enum MeisaiSection {
	ShoshinSaisin("初・再診料"),
	IgakuKanri("医学管理等"),
	Zaitaku("在宅医療"),
	Kensa("検査"),
	Gazou("画像診断"),
	Touyaku("投薬"),
	Chuusha("注射"),
	Shochi("処置"),
	Sonota("その他");

	private String label;

	MeisaiSection(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

	public static MeisaiSection fromShuukei(String shuukei){
		switch(shuukei){
			case MyclinicConsts.SHUUKEI_SHOSHIN:
			case MyclinicConsts.SHUUKEI_SAISHIN_SAISHIN:
			case MyclinicConsts.SHUUKEI_SAISHIN_GAIRAIKANRI:
			case MyclinicConsts.SHUUKEI_SAISHIN_JIKANGAI:
			case MyclinicConsts.SHUUKEI_SAISHIN_KYUUJITSU:
			case MyclinicConsts.SHUUKEI_SAISHIN_SHINYA:
				return ShoshinSaisin;
			case MyclinicConsts.SHUUKEI_SHIDO:
				return IgakuKanri;
			case MyclinicConsts.SHUUKEI_ZAITAKU:
				return Zaitaku;
			case MyclinicConsts.SHUUKEI_KENSA:
				return Kensa;
			case MyclinicConsts.SHUUKEI_GAZOSHINDAN:
				return Gazou;
			case MyclinicConsts.SHUUKEI_TOYAKU_NAIFUKUTONPUKUCHOZAI:
			case MyclinicConsts.SHUUKEI_TOYAKU_GAIYOCHOZAI:
			case MyclinicConsts.SHUUKEI_TOYAKU_SHOHO:
			case MyclinicConsts.SHUUKEI_TOYAKU_MADOKU:
			case MyclinicConsts.SHUUKEI_TOYAKU_CHOKI:
				return Touyaku;
			case MyclinicConsts.SHUUKEI_CHUSHA_SEIBUTSUETC:
			case MyclinicConsts.SHUUKEI_CHUSHA_HIKA:
			case MyclinicConsts.SHUUKEI_CHUSHA_JOMYAKU:
			case MyclinicConsts.SHUUKEI_CHUSHA_OTHERS:
				return Chuusha;
			case MyclinicConsts.SHUUKEI_SHOCHI:
				return Shochi;
			case MyclinicConsts.SHUUKEI_SHUJUTSU_SHUJUTSU:
			case MyclinicConsts.SHUUKEI_SHUJUTSU_YUKETSU:
			case MyclinicConsts.SHUUKEI_MASUI:
			case MyclinicConsts.SHUUKEI_OTHERS:
			default: return Sonota;
		}
	}
}