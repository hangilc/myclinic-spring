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
	private Integer waitState;

	public Integer getWaitState(){
		return waitState;
	}

	public void setWaitState(Integer waitState){
		this.waitState = waitState;
	}
	
	@Override
	public String toString(){
		return "Charge[" +
			"visitId=" + visitId + ", " +
			"waitState=" + waitState +
		"]";
	}

}
