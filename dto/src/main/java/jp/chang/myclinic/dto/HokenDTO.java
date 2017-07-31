package jp.chang.myclinic.dto;

public class HokenDTO {
	public ShahokokuhoDTO shahokokuho;
	public KoukikoureiDTO koukikourei;
	public RoujinDTO roujin;
	public KouhiDTO kouhi1;
	public KouhiDTO kouhi2;
	public KouhiDTO kouhi3;

	@Override
	public String toString() {
		return "HokenDTO{" +
				"shahokokuho=" + shahokokuho +
				", koukikourei=" + koukikourei +
				", roujin=" + roujin +
				", kouhi1=" + kouhi1 +
				", kouhi2=" + kouhi2 +
				", kouhi3=" + kouhi3 +
				'}';
	}
}