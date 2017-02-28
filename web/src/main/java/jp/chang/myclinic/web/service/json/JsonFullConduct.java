package jp.chang.myclinic.web.service.json;

import jp.chang.myclinic.model.Conduct;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonFullConduct {

	@JsonProperty("id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	@JsonProperty("visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	private Integer kind;

	public Integer getKind(){
		return kind;
	}

	public void setKind(Integer kind){
		this.kind = kind;
	}

	@JsonProperty("gazou_label")
	private String gazouLabel;

	public String getGazouLabel(){
		return gazouLabel;
	}

	public void setGazouLabel(String gazouLabel){
		this.gazouLabel = gazouLabel;
	}

	@JsonProperty("shinryou_list")
	private List<JsonFullConductShinryou> shinryouList;

	public List<JsonFullConductShinryou> getShinryouList(){
		return shinryouList;
	}

	public void setShinryouList(List<JsonFullConductShinryou> shinryouList){
		this.shinryouList = shinryouList;
	}

	private List<JsonFullConductDrug> drugs;

	public List<JsonFullConductDrug> getDrugs(){
		return drugs;
	}

	public void setDrugs(List<JsonFullConductDrug> arg){
		this.drugs = arg;
	}

	@JsonProperty("kizai_list")
	private List<JsonFullConductKizai> kizaiList;

	public List<JsonFullConductKizai> getKizaiList(){
		return kizaiList;
	}

	public void setKizaiList(List<JsonFullConductKizai> kizaiList){
		this.kizaiList = kizaiList;
	}

	public static JsonFullConduct create(Conduct conduct){
		JsonFullConduct dst = new JsonFullConduct();
		dst.setConductId(conduct.getConductId());
		dst.setVisitId(conduct.getVisitId());
		dst.setKind(conduct.getKind());
		return dst;
	}

}
