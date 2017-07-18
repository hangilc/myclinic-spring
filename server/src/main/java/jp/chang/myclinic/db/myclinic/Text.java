package jp.chang.myclinic.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="visit_text")
public class Text {

    @Id
    @GeneratedValue
    @Column(name="text_id")
    private Integer textId;

    @Column(name="visit_id")
    private Integer visitId;

    private String content;

    public Integer getTextId() {
        return textId;
    }

    public void setTextId(Integer textId) {
        this.textId = textId;
    }

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Text{" +
                "textId=" + textId +
                ", visitId=" + visitId +
                ", content='" + content + '\'' +
                '}';
    }
}
