package jp.chang.myclinic.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="visit_conduct")
public class Conduct {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	@Column(name="visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	private Integer kind;

	public Integer getKind(){
		return kind;
	}

	public void setKind(Integer kind){
		this.kind = kind;
	}

	@Override
	public String toString(){
		return "Conduct[" +
			"conductId=" + conductId + ", " +
			"visitId=" + visitId + ", " +
			"kind=" + kind +
		"]";
	}

}
