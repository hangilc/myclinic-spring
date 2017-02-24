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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name="visit_conduct_shinryou")
public class ConductShinryou {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer conductShinryouId;

	public Integer getConductShinryouId(){
		return conductShinryouId;
	}

	public void setConductShinryouId(Integer conductShinryouId){
		this.conductShinryouId = conductShinryouId;
	}

	@Column(name="visit_conduct_id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="visit_conduct_id", insertable=false, updatable=false)
	private Conduct conduct;

	public Conduct getConduct(){
		return conduct;
	}

	public void setConduct(Conduct conduct){
		this.conduct = conduct;
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

	public void setMasterValidFrom(Date mastervalidfrom){
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
		return "ConductShinryou[" +
			"conductShinryouId=" + conductShinryouId + ", " +
			"conductId=" + conductId + ", " +
			"shinryoucode=" + shinryoucode + ", " +
			"masterValidFrom=" + masterValidFrom +
		"]";
	}
}
