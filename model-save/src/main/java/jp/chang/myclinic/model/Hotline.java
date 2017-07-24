package jp.chang.myclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.FetchType;
import javax.persistence.Transient;
import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name="hotline")
public class Hotline {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="hotline_id")
	private Integer hotlineId;

	public Integer getHotlineId(){
		return hotlineId;
	}

	public void setHotlineId(Integer hotlineId){
		this.hotlineId = hotlineId;
	}

	private String message;

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}
	
	private String sender;

	public String getSender(){
		return sender;
	}

	public void setSender(String sender){
		this.sender = sender;
	}

	private String recipient;

	public String getRecipient(){
		return recipient;
	}

	public void setRecipient(String recipient){
		this.recipient = recipient;
	}

	@Column(name="m_datetime")
	private Timestamp at;

	public Timestamp getAt(){
		return at;
	}

	public void setAt(Timestamp at){
		this.at = at;
	}

	@Override
	public String toString(){
		return "Hotline[" +
			"hotlineId=" + hotlineId + ", " +
			"message=" + message + ", " +
			"sender=" + sender + ", " +
			"recipient=" + recipient + ", " +
			"at=" + at +
		"]";
	}

}
