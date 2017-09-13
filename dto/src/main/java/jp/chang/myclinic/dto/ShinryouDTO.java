package jp.chang.myclinic.dto;

public class ShinryouDTO {
	public int shinryouId;
	public int visitId;
	public int shinryoucode;

	public static ShinryouDTO copy(ShinryouDTO src){
		ShinryouDTO dst = new ShinryouDTO();
		dst.shinryouId = src.shinryouId;
		dst.visitId = src.visitId;
		dst.shinryoucode = src.shinryoucode;
		return dst;
	}

	@Override
	public String toString() {
		return "ShinryouDTO{" +
				"shinryouId=" + shinryouId +
				", visitId=" + visitId +
				", shinryoucode=" + shinryoucode +
				'}';
	}
}