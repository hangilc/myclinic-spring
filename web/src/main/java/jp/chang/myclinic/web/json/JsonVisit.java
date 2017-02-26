package jp.chang.myclinic.web.json;

import jp.chang.myclinic.model.Visit;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
	//@JsonSerialize(using=TimestampSerializer.class)
	//@JsonDeserialize(using=TimestampDeserializer.class)
	private String visitedAt;

	public String getVisitedAt(){
		return visitedAt;
	}

	public void setVisitedAt(String visitedAt){
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		JsonVisit json = new JsonVisit();
		json.setVisitId(visit.getVisitId());
		json.setPatientId(visit.getPatientId());
		json.setVisitedAt(visit.getVisitedAt().toLocalDateTime().format(formatter));
		json.setShahokokuhoId(visit.getShahokokuhoId());
		json.setKoukikoureiId(visit.getKoukikoureiId());
		json.setRoujinId(visit.getRoujinId());
		json.setKouhi1Id(visit.getKouhi1Id());
		json.setKouhi2Id(visit.getKouhi2Id());
		json.setKouhi3Id(visit.getKouhi3Id());
		return json;
	}

	public static Visit toVisit(JsonVisit json){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Visit visit = new Visit();
		visit.setVisitId(json.getVisitId());
		visit.setPatientId(json.getPatientId());
		visit.setVisitedAt(Timestamp.valueOf(LocalDateTime.parse(json.getVisitedAt(), formatter)));
		visit.setShahokokuhoId(json.getShahokokuhoId());
		visit.setKoukikoureiId(json.getKoukikoureiId());
		visit.setRoujinId(json.getRoujinId());
		visit.setKouhi1Id(json.getKouhi1Id());
		visit.setKouhi2Id(json.getKouhi2Id());
		visit.setKouhi3Id(json.getKouhi3Id());
		return visit;
	}
}