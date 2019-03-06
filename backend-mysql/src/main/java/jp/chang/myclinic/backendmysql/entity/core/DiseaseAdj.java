package jp.chang.myclinic.backendmysql.entity.core;

import javax.persistence.*;

@Entity
@Table(name="disease_adj")
public class DiseaseAdj {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="disease_adj_id")
    private Integer diseaseAdjId;

    @Column(name="disease_id")
    private Integer diseaseId;

    private Integer shuushokugocode;

    public Integer getDiseaseAdjId() {
        return diseaseAdjId;
    }

    public void setDiseaseAdjId(Integer diseaseAdjId) {
        this.diseaseAdjId = diseaseAdjId;
    }

    public Integer getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    public Integer getShuushokugocode() {
        return shuushokugocode;
    }

    public void setShuushokugocode(Integer shuushokugocode) {
        this.shuushokugocode = shuushokugocode;
    }

    @Override
    public String toString() {
        return "DiseaseAdj{" +
                "diseaseAdjId=" + diseaseAdjId +
                ", diseaseId=" + diseaseId +
                ", shuushokugocode=" + shuushokugocode +
                '}';
    }
}
