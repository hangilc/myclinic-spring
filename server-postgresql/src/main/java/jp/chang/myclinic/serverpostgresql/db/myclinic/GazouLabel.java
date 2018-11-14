package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="gazou_label")
public class GazouLabel {

	@Id
	@Column(name="conduct_id")
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
