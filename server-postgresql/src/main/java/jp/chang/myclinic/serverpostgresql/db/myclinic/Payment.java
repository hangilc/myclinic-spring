package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@IdClass(PaymentId.class)
@Table(name="payment")
public class Payment {

	@Id
	@Column(name="visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	private Integer amount;

	public Integer getAmount(){
		return amount;
	}

	public void setAmount(Integer amount){
		this.amount = amount;
	}
	
	@Id
	private LocalDateTime paytime;

	public LocalDateTime getPaytime(){
		return paytime;
	}

	public void setPaytime(LocalDateTime paytime){
		this.paytime = paytime;
	}
	
	@Override
	public String toString(){
		return "Charge[" +
			"visitId=" + visitId + ", " +
			"amount=" + amount + ", " +
			"paytime=" + paytime +
		"]";
	}

}
