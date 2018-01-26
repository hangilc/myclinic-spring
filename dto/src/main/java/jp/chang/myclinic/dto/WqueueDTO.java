package jp.chang.myclinic.dto;

import java.util.Objects;

public class WqueueDTO {
	public int visitId;
	public int waitState;

	@Override
	public String toString() {
		return "WqueueDTO{" +
				"visitId=" + visitId +
				", waitState=" + waitState +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WqueueDTO wqueueDTO = (WqueueDTO) o;
		return visitId == wqueueDTO.visitId &&
				waitState == wqueueDTO.waitState;
	}

	@Override
	public int hashCode() {

		return Objects.hash(visitId, waitState);
	}
}