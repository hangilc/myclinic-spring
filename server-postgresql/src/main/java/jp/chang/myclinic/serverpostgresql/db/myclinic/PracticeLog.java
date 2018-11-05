package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="practice_log")
public class PracticeLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="practice_log_id")
    private Integer practiceLogId;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    private String kind;

    private String body;

    public Integer getPracticeLogId() {
        return practiceLogId;
    }

    public void setPracticeLogId(Integer practiceLogId) {
        this.practiceLogId = practiceLogId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
