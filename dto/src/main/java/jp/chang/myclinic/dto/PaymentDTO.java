package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

public class PaymentDTO {
	@Primary
	public int visitId;
	public int amount;
	@Primary
	public String paytime;

	@Override
	public String toString() {
		return "PaymentDTO{" +
				"visitId=" + visitId +
				", amount=" + amount +
				", paytime='" + paytime + '\'' +
				'}';
	}
}