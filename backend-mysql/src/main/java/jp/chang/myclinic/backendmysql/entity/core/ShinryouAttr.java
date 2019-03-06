package jp.chang.myclinic.backendmysql.entity.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shinryou_attr")
public class ShinryouAttr {

    public ShinryouAttr(){

    }

    public ShinryouAttr(int shinryouId, String tekiyou){
        this.shinryouId = shinryouId;
        this.tekiyou = tekiyou;
    }

    @Id
    @Column(name="shinryou_id")
    private Integer shinryouId;

    private String tekiyou;

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

    public boolean isEmpty(){
        return tekiyou == null;
    }

    @Override
    public String toString() {
        return "ShinryouAttr{" +
                "shinryouId=" + shinryouId +
                ", tekiyou='" + tekiyou + '\'' +
                '}';
    }
}
