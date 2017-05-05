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

	@Override
	public String toString(){
		return "DrugDTO[" +
			"drugId" + drugId + "," +
			"visitId" + visitId + "," +
			"iyakuhincode" + iyakuhincode + "," +
			"amount" + amount + "," +
			"usage" + usage + "," +
			"days" + days + "," +
			"category" + category + "," +
			"shuukeisaki" + shuukeisaki + "," +
			"prescribed" + prescribed + //"," +
		"]";
	}
}