package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

public class ByoumeiMasterDTO {

    @Primary
    public int shoubyoumeicode;
    public String name;
    @Primary
    public String validFrom;
    public String validUpto;

    @Override
    public String toString() {
        return "ByoumeiMasterDTO{" +
                "shoubyoumeicode=" + shoubyoumeicode +
                ", name='" + name + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validUpto='" + validUpto + '\'' +
                '}';
    }
}
