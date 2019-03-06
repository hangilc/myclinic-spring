package jp.chang.myclinic.backendmysql.entity.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="drug_attr")
public class DrugAttr {

    //private static Logger logger = LoggerFactory.getLogger(DrugAttr.class);


    public DrugAttr() {
    }

    public DrugAttr(Integer drugId, String tekiyou) {
        this.drugId = drugId;
        this.tekiyou = tekiyou;
    }

    @Id
    @Column(name="drug_id")
    private Integer drugId;

    private String tekiyou;

    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    public String getTekiyou() {
        return tekiyou;
    }

    public void setTekiyou(String tekiyou) {
        this.tekiyou = tekiyou;
    }

    public boolean isEmpty(){
        return tekiyou == null;
    }

    @Override
    public String toString() {
        return "DrugAttr{" +
                "drugId=" + drugId +
                ", tekiyou='" + tekiyou + '\'' +
                '}';
    }
}
