package jp.chang.myclinic.server.rcpt;

import java.text.DecimalFormat;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.RcptUtil;

class TonpukuItem extends SectionItem {

	private DrugFullDTO drugFull;

	TonpukuItem(DrugFullDTO drugFull){
		super(drugFull.drug.days);
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