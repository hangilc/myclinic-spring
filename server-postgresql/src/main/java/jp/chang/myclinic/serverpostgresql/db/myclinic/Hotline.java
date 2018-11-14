package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="hotline")
public class Hotline {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="hotline_id")
    private Integer hotlineId;

    private String message;

    private String sender;

    private String recipient;

    @Column(name="postedAt")
    private LocalDateTime postedAt;

    public Integer getHotlineId() {
        return hotlineId;
    }

    public void setHotlineId(Integer hotlineId) {
        this.hotlineId = hotlineId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }
}
