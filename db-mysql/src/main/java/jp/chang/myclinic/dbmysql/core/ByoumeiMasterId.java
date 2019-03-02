package jp.chang.myclinic.dbmysql.core;

import java.io.Serializable;

public class ByoumeiMasterId implements Serializable {

    private Integer shoubyoumeicode;
    private String validFrom;

    public ByoumeiMasterId(){ }

    public ByoumeiMasterId(int shoubyoumeicode, String validFrom){
        this.shoubyoumeicode = shoubyoumeicode;
        this.validFrom = validFrom;
    }

    public Integer getShoubyoumeicode() {
        return shoubyoumeicode;
    }

    public String getValidFrom() {
        return validFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByoumeiMasterId that = (ByoumeiMasterId) o;

        if (!shoubyoumeicode.equals(that.shoubyoumeicode)) return false;
        return validFrom.equals(that.validFrom);
    }

    @Override
    public int hashCode() {
        int result = shoubyoumeicode.hashCode();
        result = 31 * result + validFrom.hashCode();
        return result;
    }
}
