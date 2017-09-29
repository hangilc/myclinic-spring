package jp.chang.myclinic.server.rcpt;

import java.text.DecimalFormat;

import jp.chang.myclinic.dto.*;

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