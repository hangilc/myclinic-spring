package jp.chang.myclinic.rcpt;

import java.text.DecimalFormat;

import jp.chang.myclinic.dto.*;

class GaiyouItem extends SectionItem {

	private DrugFullDTO drugFull;

	GaiyouItem(DrugFullDTO drugFull){
		super(1);
		this.drugFull = drugFull;
	}

	@Override
	public int getTanka(){
		double amount = drugFull.drug.amount;
		double yakka = drugFull.master.yakka;
		return RcptUtil.touyakuKingakuToTen(amount * yakka);
	}

	@Override
	public String getLabel(){
		String name = drugFull.master.name;
		double amount = drugFull.drug.amount;
		String unit = drugFull.master.unit;
		DecimalFormat fmt = new DecimalFormat();
		return String.format("%s %s%s", name, fmt.format(amount), unit);
	}

}