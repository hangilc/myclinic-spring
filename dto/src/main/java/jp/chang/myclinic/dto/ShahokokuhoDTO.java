package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

import java.util.Objects;

public class ShahokokuhoDTO {
	@Primary
	@AutoInc
	public int shahokokuhoId;
	public int patientId;
	public int hokenshaBangou;
	public String hihokenshaKigou;
	public String hihokenshaBangou;
	public int honnin;
	public int kourei;
	public String validFrom;
	public String validUpto;

	public static ShahokokuhoDTO copy(ShahokokuhoDTO src){
		ShahokokuhoDTO dst = new ShahokokuhoDTO();
		dst.shahokokuhoId = src.shahokokuhoId;
		dst.patientId = src.patientId;
		dst.hokenshaBangou = src.hokenshaBangou;
		dst.hihokenshaKigou = src.hihokenshaKigou;
		dst.hihokenshaBangou = src.hihokenshaBangou;
		dst.honnin = src.honnin;
		dst.kourei = src.kourei;
		dst.validFrom = src.validFrom;
		dst.validUpto = src.validUpto;
		return dst;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ShahokokuhoDTO that = (ShahokokuhoDTO) o;
		return shahokokuhoId == that.shahokokuhoId &&
				patientId == that.patientId &&
				hokenshaBangou == that.hokenshaBangou &&
				honnin == that.honnin &&
				kourei == that.kourei &&
				Objects.equals(hihokenshaKigou, that.hihokenshaKigou) &&
				Objects.equals(hihokenshaBangou, that.hihokenshaBangou) &&
				Objects.equals(validFrom, that.validFrom) &&
				Objects.equals(validUpto, that.validUpto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(shahokokuhoId, patientId, hokenshaBangou, hihokenshaKigou,
				hihokenshaBangou, honnin, kourei, validFrom, validUpto);
	}

	@Override
	public String toString(){
		return "ShahokokuhoDTO[" +
			"shahokokuhoId=" + shahokokuhoId + "," +
			"patientId=" + patientId + "," +
			"hokenshaBangou=" + hokenshaBangou + "," +
			"hihokenshaKigou=" + hihokenshaKigou + "," +
			"hihokenshaBangou=" + hihokenshaBangou + "," +
			"honnin=" + honnin + "," +
			"kourei=" + kourei + "," +
			"validFrom=" + validFrom + "," +
			"validUpto=" + validUpto +
		"]";
	}
}