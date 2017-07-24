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
// import javax.persistence.NamedEntityGraphs;
// import javax.persistence.NamedEntityGraph;
// import javax.persistence.NamedAttributeNode;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name="visit_shinryou")
public class Shinryou {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="shinryou_id")
	private Integer shinryouId;

	public Integer getShinryouId(){
		return shinryouId;
	}

	public void setShinryouId(Integer shinryouId){
		this.shinryouId = shinryouId;
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

	private Integer shinryoucode;

	public Integer getShinryoucode(){
		return shinryoucode;
	}

	public void setShinryoucode(Integer shinryoucode){
		this.shinryoucode = shinryoucode;
	}

	@Column(name="master_valid_from")
	private Date masterValidFrom;

	public Date getMasterValidFrom(){
		return masterValidFrom;
	}

	public void setMasterValidFrom(Date masterValidFrom){
		this.masterValidFrom = masterValidFrom;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="shinryoucode", referencedColumnName="shinryoucode", insertable=false, updatable=false),
		@JoinColumn(name="master_valid_from", referencedColumnName="valid_from", insertable=false, updatable=false),
	})
	private ShinryouMaster master;

	public ShinryouMaster getMaster(){
		return master;
	}

	public void setMaster(ShinryouMaster master){
		this.master = master;
	}

	@Override
	public String toString(){
		return "Shinryou[" +
			"shinryouId=" + shinryouId + ", " +
			"visitId=" + visitId + ", " +
			"shinryoucode=" + shinryoucode + ", " +
			"masterValidFrom=" + masterValidFrom +
		"]";
	}
}