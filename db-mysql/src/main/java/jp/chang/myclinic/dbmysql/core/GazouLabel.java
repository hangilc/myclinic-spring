package jp.chang.myclinic.dbmysql.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="visit_gazou_label")
public class GazouLabel {

	@Id
	@Column(name="visit_conduct_id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	private String label;

	public String getLabel(){
		return label;
	}

	public void setLabel(String label){
		this.label = label;
	}

	@Override
	public String toString(){
		return "GazouLabel[" +
			"conductId=" + conductId + ", " +
			"label=" + label +
		"]";
	}

}
