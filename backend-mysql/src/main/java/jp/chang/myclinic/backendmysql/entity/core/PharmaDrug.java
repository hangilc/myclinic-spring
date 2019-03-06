package jp.chang.myclinic.backendmysql.entity.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by hangil on 2017/06/15.
 */
@Entity
@Table(name="pharma_drug")
public class PharmaDrug {

    @Id
    @Column(name="iyakuhincode")
    private Integer iyakuhincode;

    private String description;

    private String sideeffect;

    public Integer getIyakuhincode() {
        return iyakuhincode;
    }

    public void setIyakuhincode(Integer iyakuhincode) {
        this.iyakuhincode = iyakuhincode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSideeffect() {
        return sideeffect;
    }

    public void setSideeffect(String sideeffect) {
        this.sideeffect = sideeffect;
    }

    @Override
    public String toString() {
        return "PharmaDrug{" +
                "iyakuhincode=" + iyakuhincode +
                ", description='" + description + '\'' +
                ", sideeffect='" + sideeffect + '\'' +
                '}';
    }
}

