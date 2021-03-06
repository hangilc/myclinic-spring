package jp.chang.myclinic.server.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="hoken_roujin")
public class Roujin {
	@Id
	@Column(name="roujin_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer roujinId;

	public Integer getRoujinId(){
		return roujinId;
	}

	public void setRoujinId(Integer roujinId){
		this.roujinId = roujinId;
	}

	@Column(name="patient_id")
	private Integer patientId;

	public Integer getPatientId(){
		return patientId;
	}

	public void setPatientId(Integer patientId){
		this.patientId = patientId;
	}

	private Integer shichouson;

	public Integer getShichouson(){
		return shichouson;
	}

	public void setShichouson(Integer shichouson){
		this.shichouson = shichouson;
	}

	private Integer jukyuusha;

	public Integer getJukyuusha(){
		return jukyuusha;
	}

	public void setJukyuusha(Integer jukyuusha){
		this.jukyuusha = jukyuusha;
	}

	@Column(name="futan_wari")
	private Integer futanWari;

	public Integer getFutanWari(){
		return futanWari;
	}

	public void setFutanWari(Integer futanWari){
		this.futanWari = futanWari;
	}

	@Column(name="valid_from")
	private String validFrom;

	public String getValidFrom(){
		return validFrom;
	}

	public void setValidFrom(String validFrom){
		this.validFrom = validFrom;
	}

	@Column(name="valid_upto")
	private String validUpto;

	public String getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(String validUpto){
		this.validUpto = validUpto;
	}

	@Override
	public String toString(){
		return "Roujin[roujinId=" + roujinId + ", " +
			"patientId=" + patientId + ", " +
			"shichouson=" + shichouson + ", " +
			"jukyuusha=" + jukyuusha + ", " +
			"futanWari=" + futanWari + ", " +
			"validFrom=" + validFrom + ", " +
			"validUpto=" + validUpto
		+ "]";
	}
}