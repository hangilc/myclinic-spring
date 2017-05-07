package jp.chang.myclinic.rcpt;

import java.util.List;
import java.time.LocalDate;

import jp.chang.myclinic.consts.HoukatsuKensaKind;
import jp.chang.myclinic.consts.MeisaiSection;

import jp.chang.myclinic.dto.*;

public class RcptVisit {

	private Meisai meisai = new Meisai();

	public Meisai getMeisai(){
		return meisai;
	}

	public void add(List<ShinryouFullDTO> shinryouList, HoukatsuKensa.Revision revision){
		shinryouList.forEach(shinryou -> add(shinryou, revision));
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

}