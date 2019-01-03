package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="conduct_shinryou")
public class ConductShinryou {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="conduct_shinryou_id")
	private Integer conductShinryouId;

	public Integer getConductShinryouId(){
		return conductShinryouId;
	}

	public void setConductShinryouId(Integer conductShinryouId){
		this.conductShinryouId = conductShinryouId;
	}

	@Column(name="conduct_id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	private Integer shinryoucode;

	public Integer getShinryoucode(){
		return shinryoucode;
	}

	public void setShinryoucode(Integer shinryoucode){
		this.shinryoucode = shinryoucode;
	}

	@Override
	public String toString(){
		return "ConductShinryou[" +
			"conductShinryouId=" + conductShinryouId + ", " +
			"conductId=" + conductId + ", " +
			"shinryoucode=" + shinryoucode +
		"]";
	}
}
