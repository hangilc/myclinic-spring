package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

/**
 * Created by hangil on 2017/06/15.
 */
public class PharmaDrugDTO {
    @Primary
    public int iyakuhincode;
    public String description;
    public String sideeffect;

    @Override
    public String toString() {
        return "PharmaDrugDTO{" +
                "iyakuhincode=" + iyakuhincode +
                ", description='" + description + '\'' +
                ", sideeffect='" + sideeffect + '\'' +
                '}';
    }
}
