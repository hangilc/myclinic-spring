package jp.chang.myclinic.server.db.myclinic;

import javax.persistence.*;

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

    @Column(name="m_datetime")
    private String postedAt;

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

    public String getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }
}
