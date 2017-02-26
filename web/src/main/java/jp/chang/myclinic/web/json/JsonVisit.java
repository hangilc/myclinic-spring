package jp.chang.myclinic.web.json;

import jp.chang.myclinic.model.Visit;
import java.sql.Timestamp;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonVisit {

	@JsonProperty("visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	@JsonProperty("patient_id")
	private Integer patientId;

	public Integer getPatientId(){
		return patientId;
	}

	public void setPatientId(Integer patientId){
		this.patientId = patientId;
	}

	@JsonProperty("v_datetime")
	@JsonSerialize(using=TimestampSerializer.class)
	private Timestamp visitedAt;

	public Timestamp getVisitedAt(){
		return visitedAt;
	}

	public void setVisitedAt(Timestamp visitedAt){
		this.visitedAt = visitedAt;
	}

	@JsonProperty("shahokokuho_id")
	private Integer shahokokuhoId;

	public Integer getShahokokuhoId(){
		return shahokokuhoId;
	}

	public void setShahokokuhoId(Integer shahokokuhoId){
		this.shahokokuhoId = shahokokuhoId;
	}
	
	@JsonProperty("roujin_id")
	private Integer roujinId;

	public Integer getRoujinId(){
		return roujinId;
	}

	public void setRoujinId(Integer roujinId){
		this.roujinId = roujinId;
	}
	
	@JsonProperty("koukikourei_id")
	private Integer koukikoureiId;

	public Integer getKoukikoureiId(){
		return koukikoureiId;
	}

	public void setKoukikoureiId(Integer koukikoureiId){
		this.koukikoureiId = koukikoureiId;
	}
	
	@JsonProperty("kouhi_1_id")
	private Integer kouhi1Id;

	public Integer getKouhi1Id(){
		return kouhi1Id;
	}

	public void setKouhi1Id(Integer kouhi1Id){
		this.kouhi1Id = kouhi1Id;
	}

	@JsonProperty("kouhi_2_id")
	private Integer kouhi2Id;

	public Integer getKouhi2Id(){
		return kouhi2Id;
	}

	public void setKouhi2Id(Integer kouhi2Id){
		this.kouhi2Id = kouhi2Id;
	}

	@JsonProperty("kouhi_3_id")
	private Integer kouhi3Id;

	public Integer getKouhi3Id(){
		return kouhi3Id;
	}

	public void setKouhi3Id(Integer kouhi3Id){
		this.kouhi3Id = kouhi3Id;
	}

	public static JsonVisit fromVisit(Visit visit){
		JsonVisit json = new JsonVisit();
		json.setVisitId(visit.getVisitId());
		json.setPatientId(visit.getPatientId());
		json.setVisitedAt(visit.getVisitedAt());
		json.setShahokokuhoId(visit.getShahokokuhoId());
		json.setKoukikoureiId(visit.getKoukikoureiId());
		json.setRoujinId(visit.getRoujinId());
		json.setKouhi1Id(visit.getKouhi1Id());
		json.setKouhi2Id(visit.getKouhi2Id());
		json.setKouhi3Id(visit.getKouhi3Id());
		return json;
	}
}