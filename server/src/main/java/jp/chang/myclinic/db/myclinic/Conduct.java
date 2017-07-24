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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="visit_id", insertable=false, updatable=false)
	private Visit visit;

	public Visit getVisit(){
		return visit;
	}

	public void setVisit(Visit visit){
		this.visit = visit;
	}

	private Integer kind;

	public Integer getKind(){
		return kind;
	}

	public void setKind(Integer kind){
		this.kind = kind;
	}

	// @OneToOne(fetch=FetchType.EAGER)
	// @JoinColumn(name="id", insertable=false, updatable=false)
	// @NotFound(action=NotFoundAction.IGNORE)
	// private GazouLabel gazouLabel;

	// public GazouLabel getGazouLabel(){
	// 	return gazouLabel;
	// }

	// public void setGazouLabel(GazouLabel gazouLabel){
	// 	this.gazouLabel = gazouLabel;
	// }

	@Override
	public String toString(){
		return "Conduct[" +
			"conductId=" + conductId + ", " +
			"visitId=" + visitId + ", " +
			"kind=" + kind +
		"]";
	}

}
