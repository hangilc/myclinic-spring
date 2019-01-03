package jp.chang.myclinic.serverpostgresql.db.myclinic;

import java.io.Serializable;
import java.time.LocalDate;

public class ByoumeiMasterId implements Serializable {

    private Integer shoubyoumeicode;
    private LocalDate validFrom;

    public ByoumeiMasterId(){ }

    public ByoumeiMasterId(int shoubyoumeicode, LocalDate validFrom){
        this.shoubyoumeicode = shoubyoumeicode;
        this.validFrom = validFrom;
    }

    public Integer getShoubyoumeicode() {
        return shoubyoumeicode;
    }

    public LocalDate getValidFrom() {
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
