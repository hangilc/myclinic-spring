package jp.chang.myclinic.dto;

public class ShinryouMasterDTO {
	public int shinryoucode;
	public String validFrom;
	public String name;
	public int tensuu;
	public char tensuuShikibetsu;
	public String shuukeisaki;
	public String houkatsukensa;
	public char oushinkubun;
	public String kensaGroup;
	//public char roujinTekiyou;
	public char codeShou;
	public String codeBu;
	public char codeAlpha;
	public String codeKubun;
	public String validUpto;

	@Override
	public String toString(){
		return "ShinryouMasterDTO[" +
			"shinryoucode=" + shinryoucode + "," +
			"validFrom=" + validFrom + "," +
			"name=" + name + "," +
			"tensuu=" + tensuu + "," +
			"tensuuShikibetsu=" + tensuuShikibetsu + "," +
			"shuukeisaki=" + shuukeisaki + "," +
			"houkatsukensa=" + houkatsukensa + "," +
			"oushinkubun=" + oushinkubun + "," +
			"kensaGroup=" + kensaGroup + "," +
			//"roujinTekiyou=" + roujinTekiyou + "," +
			"codeShou=" + codeShou + "," +
			"codeBu=" + codeBu + "," +
			"codeAlpha=" + codeAlpha + "," +
			"codeKubun=" + codeKubun + "," +
			"validUpto=" + validUpto +
		"]";
	}
}