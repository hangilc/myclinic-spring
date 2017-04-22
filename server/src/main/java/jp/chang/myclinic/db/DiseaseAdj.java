package jp.chang.myclinic.db;

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
@Table(name="disease_adj")
public class DiseaseAdj {

	@Id
	@GeneratedValue
	@Column(name="disease_adj_id")
	private Integer diseaseAdjId;

	public Integer getDiseaseAdjId(){
		return diseaseAdjId;
	}

	public void setDiseaseAdjId(Integer diseaseAdjId){
		this.diseaseAdjId = diseaseAdjId;
	}

	@Column(name="disease_id")
	private Integer diseaseId;

	public Integer getDiseaseId(){
		return diseaseId;
	}

	public void setDiseaseId(Integer diseaseId){
		this.diseaseId = diseaseId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="disease_id", insertable=false, updatable=false)
	private Disease disease;

	public Disease getDisease(){
		return disease;
	}

	public void setDisease(Disease disease){
		this.disease = disease;
	}

	@Column(name="shuushokugocode")
	private Integer shuushokugocode;

	public Integer getShuushokugocode(){
		return shuushokugocode;
	}

	public void setShuushokugocode(Integer shuushokugocode){
		this.shuushokugocode = shuushokugocode;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="shuushokugocode", referencedColumnName="shuushokugocode", insertable=false, updatable=false)
	private ShuushokugoMaster master;

	public ShuushokugoMaster getMaster(){
		return master;
	}

	public void setMaster(ShuushokugoMaster master){
		this.master = master;
	}

	@Override
	public String toString(){
		return "DiseaseAdj[" +
			"diseaseAdjId=" + diseaseAdjId + ", " +
			"diseaseId=" + diseaseId + ", " +
			"shuushokugocode=" + shuushokugocode +
		"]";
	}
}
