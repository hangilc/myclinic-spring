package jp.chang.myclinic.server.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="practice_log")
public class PracticeLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="practice_log_id")
    private Integer practiceLogId;

    @Column(name="practice_date")
    private String date;

    private String kind;

    private String body;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPracticeLogId() {
        return practiceLogId;
    }

    public void setPracticeLogId(Integer practiceLogId) {
        this.practiceLogId = practiceLogId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
