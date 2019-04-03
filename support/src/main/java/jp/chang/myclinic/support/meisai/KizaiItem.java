package jp.chang.myclinic.support.meisai;

import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.util.RcptUtil;

import java.text.DecimalFormat;

class KizaiItem extends SectionItem {

	private ConductKizaiFullDTO kizaiFull;

	KizaiItem(ConductKizaiFullDTO kizaiFull){
		super(1);
		this.kizaiFull = kizaiFull;
	}

	@Override
	public int getTanka(){
		return RcptUtil.kizaiKingakuToTen(kizaiFull.master.kingaku * kizaiFull.conductKizai.amount);
	}

	@Override
	public String getLabel(){
		DecimalFormat fmt = new DecimalFormat();
		return String.format("%s %s%s", kizaiFull.master.name,
			fmt.format(kizaiFull.conductKizai.amount), 
			kizaiFull.master.unit);
	}

}