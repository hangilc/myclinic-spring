package jp.chang.myclinic.db;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.FetchType;
import javax.persistence.Transient;
import java.sql.Date;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name="visit_conduct")
public class Conduct {

	@Id
	@GeneratedValue
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
