package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="roujin")
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
	private LocalDate validFrom;

	public LocalDate getValidFrom(){
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom){
		this.validFrom = validFrom;
	}

	@Column(name="valid_upto")
	private LocalDate validUpto;

	public LocalDate getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(LocalDate validUpto){
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