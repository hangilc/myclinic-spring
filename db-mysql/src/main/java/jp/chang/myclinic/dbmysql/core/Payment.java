package jp.chang.myclinic.dbmysql.core;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@IdClass(PaymentId.class)
@Table(name="visit_payment")
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
	private Timestamp paytime;

	public Timestamp getPaytime(){
		return paytime;
	}

	public void setPaytime(Timestamp paytime){
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
