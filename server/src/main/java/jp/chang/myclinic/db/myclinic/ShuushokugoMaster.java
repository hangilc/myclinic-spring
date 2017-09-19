package jp.chang.myclinic.db.myclinic;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shuushokugo_master")
class ShuushokugoMaster {
    @Id
    private Integer shuushokugocode;
    private String name;

    public Integer getShuushokugocode() {
        return shuushokugocode;
    }

    public void setShuushokugocode(Integer shuushokugocode) {
        this.shuushokugocode = shuushokugocode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ShuushokugoMaster{" +
                "shuushokugocode=" + shuushokugocode +
                ", name='" + name + '\'' +
                '}';
    }
}
