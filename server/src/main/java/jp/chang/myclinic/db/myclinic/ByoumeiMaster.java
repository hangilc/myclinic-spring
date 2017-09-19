package jp.chang.myclinic.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="shoubyoumei_master_arch")
@IdClass(ByoumeiMasterId.class)
class ByoumeiMaster {

    @Id
    private Integer shoubyoumeicode;
    @Id
    @Column(name="valid_from")
    private String validFrom;

    private String name;
    @Column(name="valid_upto")
    private String validUpto;

    public Integer getShoubyoumeicode() {
        return shoubyoumeicode;
    }

    public void setShoubyoumeicode(Integer shoubyoumeicode) {
        this.shoubyoumeicode = shoubyoumeicode;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(String validUpto) {
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

