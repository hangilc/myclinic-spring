package jp.chang.myclinic.dto;

public class HokenDTO {
	public ShahokokuhoDTO shahokokuho;
	public KoukikoureiDTO koukikourei;
	public RoujinDTO roujin;
	public KouhiDTO kouhi1;
	public KouhiDTO kouhi2;
	public KouhiDTO kouhi3;

	public static HokenDTO copy(HokenDTO src){
		HokenDTO dst = new HokenDTO();
		dst.shahokokuho = src.shahokokuho;
		dst.koukikourei = src.koukikourei;
		dst.roujin = src.roujin;
		dst.kouhi1 = src.kouhi1;
		dst.kouhi2 = src.kouhi2;
		dst.kouhi3 = src.kouhi3;
		return dst;
	}

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