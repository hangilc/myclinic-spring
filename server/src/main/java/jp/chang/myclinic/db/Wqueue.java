package jp.chang.myclinic.db;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name="wqueue")
public class Wqueue {

	@Id
	@Column(name="visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	@Column(name="wait_state")
	@Enumerated(EnumType.ORDINAL)
	private WqueueState waitState;

	public WqueueState getWaitState(){
		return waitState;
	}

	public void setWaitState(WqueueState waitState){
		this.waitState = waitState;
	}

	@OneToOne
	@PrimaryKeyJoinColumn
	private Visit visit;

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	@Override
	public String toString(){
		return "Charge[" +
			"visitId=" + visitId + ", " +
			"waitState=" + waitState +
		"]";
	}

}
