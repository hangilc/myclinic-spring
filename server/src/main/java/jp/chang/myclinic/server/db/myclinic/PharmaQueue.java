package jp.chang.myclinic.server.db.myclinic;

import javax.persistence.*;

/**
 * Created by hangil on 2017/06/04.
 */
@Entity
@Table(name="pharma_queue")
public class PharmaQueue {

    @Id
    @Column(name="visit_id")
    private Integer visitId;

    @Column(name="pharma_state")
    private Integer pharmaState;

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public Integer getPharmaState() {
        return pharmaState;
    }

    public void setPharmaState(Integer pharmaState) {
        this.pharmaState = pharmaState;
    }

    @Override
    public String toString() {
        return "PharmaQueue{" +
                "visitId=" + visitId +
                ", pharmaState=" + pharmaState +
                '}';
    }
}
