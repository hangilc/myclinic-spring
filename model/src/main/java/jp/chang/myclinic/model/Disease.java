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
@Table(name="disease")
public class Disease {

	@Id
	@GeneratedValue
	@Column(name="disease_id")
	private Integer diseaseId;

	public Integer getDiseaseId(){
		return diseaseId;
	}

	public void setDiseaseId(Integer diseaseId){
		this.diseaseId = diseaseId;
	}

	@Column(name="patient_id")
	private Integer patientId;

	public Integer getPatientId(){
		return patientId;
	}

	public void setPatientId(Integer patientId){
		this.patientId = patientId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="patient_id", insertable=false, updatable=false)
	private Patient patient;

	public Patient getPatient(){
		return patient;
	}

	public void setPatient(Patient patient){
		this.patient = patient;
	}

	@Column(name="shoubyoumeicode")
	private Integer shoubyoumeicode;

	public Integer getShoubyoumeicode(){
		return shoubyoumeicode;
	}

	public void setShoubyoumeicode(Integer shoubyoumeicode){
		this.shoubyoumeicode = shoubyoumeicode;
	}

	@Column(name="start_date")
	private Date startDate;

	public Date getStartDate(){
		return startDate;
	}

	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}

	@Column(name="end_date")
	private Date endDate;

	public Date getEndDate(){
		return endDate;
	}

	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}

	@Column(name="end_reason")
	private Character endReason;

	public Character getEndReason(){
		return endReason;
	}

	public void setEndReason(Character endReason){
		this.endReason = endReason;
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
		@JoinColumn(name="shoubyoumeicode", referencedColumnName="shoubyoumeicode", insertable=false, updatable=false),
		@JoinColumn(name="master_valid_from", referencedColumnName="valid_from", insertable=false, updatable=false),
	})
	private ShoubyoumeiMaster master;

	public ShoubyoumeiMaster getMaster(){
		return master;
	}

	public void setMaster(ShoubyoumeiMaster master){
		this.master = master;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy="disease")
	private List<DiseaseAdj> adjList;

	public List<DiseaseAdj> getAdjList(){
		return adjList;
	}

	public void setDiseaseAdj(List<DiseaseAdj> adjList){
		this.adjList = adjList;
	}

	@Override
	public String toString(){
		return "Disease[" +
			"diseaseId=" + diseaseId + ", " +
			"patientId=" + patientId + ", " +
			"shoubyoumeicode=" + shoubyoumeicode + ", " +
			"startDate=" + startDate + ", " +
			"endDate=" + endDate + ", " +
			"endReason=" + endReason + ", " +
			"masterValidFrom=" + masterValidFrom +
		"]";
	}
}
