package jp.chang.myclinic.db;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.Transient;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedAttributeNode;

import java.sql.Timestamp;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(
		name="patient",
		attributeNodes={@NamedAttributeNode("patient")}
	)
})
@Table(name="visit")
public class Visit {
	@Id
	@GeneratedValue
	@Column(name="visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
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

	@Column(name="v_datetime")
	private Timestamp visitedAt;

	public Timestamp getVisitedAt(){
		return visitedAt;
	}

	public void setVisitedAt(Timestamp visitedAt){
		this.visitedAt = visitedAt;
	}

	@Column(name="shahokokuho_id")
	private Integer shahokokuhoId = 0;

	public Integer getShahokokuhoId(){
		return shahokokuhoId;
	}

	public void setShahokokuhoId(Integer shahokokuhoId){
		this.shahokokuhoId = shahokokuhoId;
	}
	
	@Column(name="roujin_id")
	private Integer roujinId = 0;

	public Integer getRoujinId(){
		return roujinId;
	}

	public void setRoujinId(Integer roujinId){
		this.roujinId = roujinId;
	}
	
	@Column(name="koukikourei_id")
	private Integer koukikoureiId = 0;

	public Integer getKoukikoureiId(){
		return koukikoureiId;
	}

	public void setKoukikoureiId(Integer koukikoureiId){
		this.koukikoureiId = koukikoureiId;
	}
	
	@Column(name="kouhi_1_id")
	private Integer kouhi1Id = 0;

	public Integer getKouhi1Id(){
		return kouhi1Id;
	}

	public void setKouhi1Id(Integer kouhi1Id){
		this.kouhi1Id = kouhi1Id;
	}

	@Column(name="kouhi_2_id")
	private Integer kouhi2Id = 0;

	public Integer getKouhi2Id(){
		return kouhi2Id;
	}

	public void setKouhi2Id(Integer kouhi2Id){
		this.kouhi2Id = kouhi2Id;
	}

	@Column(name="kouhi_3_id")
	private Integer kouhi3Id = 0;

	public Integer getKouhi3Id(){
		return kouhi3Id;
	}

	public void setKouhi3Id(Integer kouhi3Id){
		this.kouhi3Id = kouhi3Id;
	}

	// @OneToMany(fetch=FetchType.LAZY, mappedBy="visit")
	// private List<Text> texts;

	// public List<Text> getTexts(){
	// 	return texts;
	// }

	// public void setTexts(List<Text> texts){
	// 	this.texts = texts;
	// }

	// @OneToMany(fetch=FetchType.LAZY, mappedBy="visit")
	// private List<Drug> drugs;

	// public List<Drug> getDrugs(){
	// 	return drugs;
	// }

	// public void setDrugs(List<Drug> drugs){
	// 	this.drugs = drugs;
	// }

	// @OneToMany(fetch=FetchType.LAZY, mappedBy="visit")
	// private List<Shinryou> shinryouList;

	// public List<Shinryou> getShinryouList(){
	// 	return shinryouList;
	// }

	// public void setShinryouList(List<Shinryou> shinryouList){
	// 	this.shinryouList = shinryouList;
	// }

	@Override
	public String toString(){
		return "Visit[" +
			"visitId=" + visitId + ", " +
			"patientId=" + patientId + ", " +
			"visitedAt=" + visitedAt + ", " +
			"shahokokuhoId=" + shahokokuhoId + ", " +
			"koukikoureiId=" + koukikoureiId + ", " +
			"roujinId=" + roujinId + ", " +
			"kouhi1Id=" + kouhi1Id + ", " +
			"kouhi2Id=" + kouhi2Id + ", " +
			"kouhi3Id=" + kouhi3Id 
		+ "]";
	}

}
