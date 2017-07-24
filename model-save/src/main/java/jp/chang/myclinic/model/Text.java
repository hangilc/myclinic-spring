package jp.chang.myclinic.model;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="visit_text")
public class Text {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="text_id")
	private Integer textId;

	public Integer getTextId(){
		return textId;
	}

	public void setTextId(Integer textId){
		this.textId = textId;
	}

	@Column(name="visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="visit_id", insertable=false, updatable=false)
	private Visit visit;

	public Visit getVisit(){
		return visit;
	}

	public void setVisit(Visit visit){
		this.visit = visit;
	}

	@Column(name="content")
	private String content;

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	@Override
	public String toString(){
		return "Text[" +
			"textId=" + textId + ", " +
			"visitId=" + visitId + ", " +
			"content=" + content + 
			"]";
	}
}