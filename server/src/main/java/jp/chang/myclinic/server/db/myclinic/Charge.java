package jp.chang.myclinic.server.db.myclinic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name="visit_charge")
public class Charge {

	@Id
	@Column(name="visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	private Integer charge;

	public Integer getCharge(){
		return charge;
	}

	public void setCharge(Integer charge){
		this.charge = charge;
	}
	
	@Override
	public String toString(){
		return "Charge[" +
			"visitId=" + visitId + ", " +
			"charge=" + charge +
		"]";
	}

}
