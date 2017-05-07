package jp.chang.myclinic.rcpt;

import java.util.List;
import java.time.LocalDate;

import jp.chang.myclinic.consts.HoukatsuKensaKind;
import jp.chang.myclinic.consts.MeisaiSection;
import jp.chang.myclinic.consts.DrugCategory;

import jp.chang.myclinic.dto.*;

public class RcptVisit {

	private Meisai meisai = new Meisai();

	public Meisai getMeisai(){
		return meisai;
	}

	public void add(List<ShinryouFullDTO> shinryouList, HoukatsuKensa.Revision revision){
		shinryouList.forEach(shinryou -> add(shinryou, revision));
	}

	public void add(List<DrugFullDTO> drugs){
		drugs.forEach(this::add);
	}

	public void add(ShinryouFullDTO shinryou, HoukatsuKensa.Revision revision){
		ShinryouMasterDTO master = shinryou.master;
		HoukatsuKensaKind kind = HoukatsuKensaKind.fromCode(master.houkatsukensa);
		if( kind == HoukatsuKensaKind.NONE ){
			SimpleShinryouItem item = new SimpleShinryouItem(master.shinryoucode, master.tensuu, master.name);
			MeisaiSection section = MeisaiSection.fromShuukei(master.shuukeisaki);
			meisai.add(section, item);
		} else {
			HoukatsuKensaItem item = new HoukatsuKensaItem(kind, master, revision);
			meisai.add(MeisaiSection.Kensa, item);
		}
	}

	public void add(DrugFullDTO drugFull){
		DrugDTO drug = drugFull.drug;
		IyakuhinMasterDTO master = drugFull.master;
		DrugCategory category = DrugCategory.fromCode(drug.category);
		switch(category){
			case Naifuku: {
				NaifukuItem item = new NaifukuItem(drugFull);
				meisai.add(MeisaiSection.Touyaku, item);
				break;
			}
			case Tonpuku: {
				TonpukuItem item = new TonpukuItem(drugFull);
				meisai.add(MeisaiSection.Touyaku, item);
				break;
			}
			case Gaiyou: break;
			default: System.out.println("Unknown category (neglected): " + drug.category);
		}
	}

}