package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="visit")
public class Visit {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
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

	@Column(name="visited_at")
	private LocalDateTime visitedAt;

	public LocalDateTime getVisitedAt(){
		return visitedAt;
	}

	public void setVisitedAt(LocalDateTime visitedAt){
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
