package jp.chang.myclinic.web.service.json;

import jp.chang.myclinic.model.Visit;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonFullVisit {
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

	private JsonShahokokuho shahokokuho;

	public JsonShahokokuho getShahokokuho() {
		return shahokokuho;
	}

	public void setShahokokuho(JsonShahokokuho shahokokuho) {
		this.shahokokuho = shahokokuho;
	}

	@JsonProperty("roujin_id")
	private Integer roujinId;

	public Integer getRoujinId(){
		return roujinId;
	}

	public void setRoujinId(Integer roujinId){
		this.roujinId = roujinId;
	}

	private JsonRoujin roujin;

	public JsonRoujin getRoujin() {
		return roujin;
	}

	public void setRoujin(JsonRoujin roujin) {
		this.roujin = roujin;
	}

	@JsonProperty("koukikourei_id")
	private Integer koukikoureiId;

	public Integer getKoukikoureiId(){
		return koukikoureiId;
	}

	public void setKoukikoureiId(Integer koukikoureiId){
		this.koukikoureiId = koukikoureiId;
	}

	private JsonKoukikourei koukikourei;

	public JsonKoukikourei getKoukikourei() {
		return koukikourei;
	}

	public void setKoukikourei(JsonKoukikourei koukikourei) {
		this.koukikourei = koukikourei;
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

	@JsonProperty("kouhi_list")
	private List<JsonKouhi> kouhiList;

	public List<JsonKouhi> getKouhiList() {
		return kouhiList;
	}

	public void setKouhiList(List<JsonKouhi> kouhiList) {
		this.kouhiList = kouhiList;
	}

	private List<JsonText> texts;

	public List<JsonText> getTexts(){
		return texts;
	}

	public void setTexts(List<JsonText> texts){
		this.texts = texts;
	}

	private List<JsonFullDrug> drugs;

	public List<JsonFullDrug> getDrugs(){
		return drugs;
	}

	public void setDrugs(List<JsonFullDrug> drugs){
		this.drugs = drugs;
	}

	@JsonProperty("shinryou_list")
	private List<JsonFullShinryou> shinryouList;

	public List<JsonFullShinryou> getShinryouList(){
		return shinryouList;
	}

	public void setShinryouList(List<JsonFullShinryou> shinryouList){
		this.shinryouList = shinryouList;
	}

	private List<JsonFullConduct> conducts;

	public List<JsonFullConduct> getConducts(){
		return conducts;
	}

	public void setConducts(List<JsonFullConduct> conducts){
		this.conducts = conducts;
	}


	public static void stuff(JsonFullVisit dst, Visit src){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		dst.setVisitId(src.getVisitId());
		dst.setPatientId(src.getPatientId());
		dst.setVisitedAt(src.getVisitedAt().toLocalDateTime().format(formatter));
		dst.setShahokokuhoId(src.getShahokokuhoId());
		dst.setKoukikoureiId(src.getKoukikoureiId());
		dst.setRoujinId(src.getRoujinId());
		dst.setKouhi1Id(src.getKouhi1Id());
		dst.setKouhi2Id(src.getKouhi2Id());
		dst.setKouhi3Id(src.getKouhi3Id());
	}

}
