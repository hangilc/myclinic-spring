package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="byoumei_master")
@IdClass(ByoumeiMasterId.class)
class ByoumeiMaster {

    @Id
    private Integer shoubyoumeicode;
    @Id
    @Column(name="valid_from")
    private LocalDate validFrom;

    private String name;
    @Column(name="valid_upto")
    private LocalDate validUpto;

    public Integer getShoubyoumeicode() {
        return shoubyoumeicode;
    }

    public void setShoubyoumeicode(Integer shoubyoumeicode) {
        this.shoubyoumeicode = shoubyoumeicode;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(LocalDate validUpto) {
        this.validUpto = validUpto;
    }

    @Override
    public String toString() {
        return "ByoumeiMaster{" +
                "shoubyoumeicode=" + shoubyoumeicode +
                ", validFrom='" + validFrom + '\'' +
                ", name='" + name + '\'' +
                ", validUpto='" + validUpto + '\'' +
                '}';
    }
}

