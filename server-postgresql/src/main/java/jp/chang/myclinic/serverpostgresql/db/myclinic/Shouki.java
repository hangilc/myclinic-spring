package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shouki")
public class Shouki {

    public Shouki() {

    }

    public Shouki(int visitId, String shouki){
        this.visitId = visitId;
        this.shouki = shouki;
    }

    @Id
    @Column(name="visit_id")
    private Integer visitId;

    private String shouki;

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public String getShouki() {
        return shouki;
    }

    public void setShouki(String shouki) {
        this.shouki = shouki;
    }

    @Override
    public String toString() {
        return "Shouki{" +
                "visitId=" + visitId +
                ", shouki='" + shouki + '\'' +
                '}';
    }
}
