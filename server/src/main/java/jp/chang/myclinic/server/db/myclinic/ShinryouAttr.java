package jp.chang.myclinic.server.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="shinryou_attr")
public class ShinryouAttr {

    @Id
    @Column(name="shinryou_id")
    private Integer shinryouId;

    private String tekiyou;
    @Column(name="shoujou_shouki")
    private String shoujouShouki;

    public Integer getShinryouId() {
        return shinryouId;
    }

    public void setShinryouId(Integer shinryouId) {
        this.shinryouId = shinryouId;
    }

    public String getTekiyou() {
        return tekiyou;
    }

    public void setTekiyou(String tekiyou) {
        this.tekiyou = tekiyou;
    }

    public String getShoujouShouki() {
        return shoujouShouki;
    }

    public void setShoujouShouki(String shoujouShouki) {
        this.shoujouShouki = shoujouShouki;
    }

    @Override
    public String toString() {
        return "ShinryouAttr{" +
                "shinryouId=" + shinryouId +
                ", tekiyou='" + tekiyou + '\'' +
                ", shoujouShouki='" + shoujouShouki + '\'' +
                '}';
    }
}
