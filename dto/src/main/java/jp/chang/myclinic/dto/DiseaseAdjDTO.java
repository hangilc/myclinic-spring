package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

import java.util.Objects;

public class DiseaseAdjDTO {
    @Primary @AutoInc
    public int diseaseAdjId;
    public int diseaseId;
    public int shuushokugocode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiseaseAdjDTO that = (DiseaseAdjDTO) o;
        return diseaseAdjId == that.diseaseAdjId &&
                diseaseId == that.diseaseId &&
                shuushokugocode == that.shuushokugocode;
    }

    @Override
    public int hashCode() {

        return Objects.hash(diseaseAdjId, diseaseId, shuushokugocode);
    }

    @Override
    public String toString() {
        return "DiseaseAdjDTO{" +
                "diseaseAdjId=" + diseaseAdjId +
                ", diseaseId=" + diseaseId +
                ", shuushokugocode=" + shuushokugocode +
                '}';
    }
}
