package jp.chang.myclinic.web.json;

import jp.chang.myclinic.model.Text;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonText {

	@JsonProperty("text_id")
	private Integer textId;

	public Integer getTextId(){
		return textId;
	}

	public void setTextId(Integer textId){
		this.textId = textId;
	}

	@JsonProperty("visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	private String content;

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public static JsonText fromText(Text src){
		JsonText dst = new JsonText();
		dst.setTextId(src.getTextId());
		dst.setVisitId(src.getVisitId());
		dst.setContent(src.getContent());
		return dst;
	}

	public static Text toText(JsonText src){
		Text dst = new Text();
		dst.setTextId(src.getTextId());
		dst.setVisitId(src.getVisitId());
		dst.setContent(src.getContent());
		return dst;
	}
}