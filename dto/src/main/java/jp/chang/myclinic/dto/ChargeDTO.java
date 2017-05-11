package jp.chang.myclinic.dto;

public class ChargeDTO {
	public int visitId;
	public int charge;

	@Override
	public String toString() {
		return "ChargeDTO{" +
				"visitId=" + visitId +
				", charge=" + charge +
				'}';
	}
}