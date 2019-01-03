package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;


@Entity
@Table(name="shinryou")
public class Shinryou {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="shinryou_id")
	private Integer shinryouId;

	public Integer getShinryouId(){
		return shinryouId;
	}

	public void setShinryouId(Integer shinryouId){
		this.shinryouId = shinryouId;
	}

	@Column(name="visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
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
		return "Shinryou[" +
			"shinryouId=" + shinryouId + ", " +
			"visitId=" + visitId + ", " +
			"shinryoucode=" + shinryoucode +
		"]";
	}
}