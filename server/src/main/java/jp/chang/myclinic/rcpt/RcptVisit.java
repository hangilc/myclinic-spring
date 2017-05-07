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

	public void addShinryouList(List<ShinryouFullDTO> shinryouList, HoukatsuKensa.Revision revision){
		shinryouList.forEach(shinryou -> addShinryou(shinryou, revision));
	}

	public void addDrugs(List<DrugFullDTO> drugs){
		drugs.forEach(this::addDrug);
	}

	public void addConducts(List<ConductFullDTO> conducts){
		conducts.forEach(conduct -> {
			conduct.conductShinryouList.forEach(this::addConductShinryou);
			conduct.conductDrugs.forEach(this::addConductDrug);
			conduct.conductKizaiList.forEach(this::addConductKizai);
		});
	}

	public void addShinryou(ShinryouFullDTO shinryou, HoukatsuKensa.Revision revision){
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

	public void addDrug(DrugFullDTO drugFull){
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
			case Gaiyou: {
				GaiyouItem item = new GaiyouItem(drugFull);
				meisai.add(MeisaiSection.Touyaku, item);
				break;
			}
			default: System.out.println("Unknown category (neglected): " + drug.category);
		}
	}

	public void addConductShinryou(ConductShinryouFullDTO conductShinryou){
		throw new RuntimeException("not implemented");
	}

	public void addConductDrug(ConductDrugFullDTO conductDrug){
		throw new RuntimeException("not implemented");
	}

	public void addConductKizai(ConductKizaiFullDTO conductKizai){
		throw new RuntimeException("not implemented");
	}

}