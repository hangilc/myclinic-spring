package jp.chang.myclinic.support.meisai;

import jp.chang.myclinic.consts.MeisaiSection;
import jp.chang.myclinic.dto.ConductDrugFullDTO;
import jp.chang.myclinic.util.RcptUtil;

import java.text.DecimalFormat;

class ConductDrugItem extends SectionItem {

	private ConductDrugFullDTO conductDrugFull;
	private MeisaiSection sect;

	ConductDrugItem(ConductDrugFullDTO conductDrugFull, MeisaiSection sect){
		super(1);
		this.conductDrugFull = conductDrugFull;
		this.sect = sect;
	}

	@Override
	public int getTanka(){
		double kingaku = conductDrugFull.master.yakka * conductDrugFull.conductDrug.amount;
		if( sect == MeisaiSection.Gazou ){
			return RcptUtil.shochiKingakuToTen(kingaku);
		} else {
			return RcptUtil.touyakuKingakuToTen(kingaku);
		}
	}

	@Override
	public String getLabel(){
		DecimalFormat fmt = new DecimalFormat();
		return String.format("%s %s%s", conductDrugFull.master.name,
			fmt.format(conductDrugFull.conductDrug.amount),
			conductDrugFull.master.unit);
	}

}