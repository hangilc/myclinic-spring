package jp.chang.myclinic.db.myclinic;

import javax.persistence.*;

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
		return "Wqueue[" +
			"visitId=" + visitId + ", " +
			"waitState=" + waitState +
		"]";
	}

}
