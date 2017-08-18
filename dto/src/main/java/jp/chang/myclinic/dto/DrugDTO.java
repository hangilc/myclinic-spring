package jp.chang.myclinic.dto;

public class DrugDTO {
	public int drugId;
	public int visitId;
	public int iyakuhincode;
	public double amount;
	public String usage;
	public int days;
	public int category;
	public int shuukeisaki;
	public int prescribed;

	public static DrugDTO copy(DrugDTO src){
		DrugDTO dst = new DrugDTO();
		dst.drugId = src.drugId;
		dst.visitId = src.visitId;
		dst.iyakuhincode = src.iyakuhincode;
		dst.amount = src.amount;
		dst.usage = src.usage;
		dst.days = src.days;
		dst.category = src.category;
		dst.shuukeisaki = src.shuukeisaki;
		dst.prescribed = src.prescribed;
		return dst;
	}

	@Override
	public String toString() {
		return "DrugDTO{" +
				"drugId=" + drugId +
				", visitId=" + visitId +
				", iyakuhincode=" + iyakuhincode +
				", amount=" + amount +
				", usage='" + usage + '\'' +
				", days=" + days +
				", category=" + category +
				", shuukeisaki=" + shuukeisaki +
				", prescribed=" + prescribed +
				'}';
	}
}