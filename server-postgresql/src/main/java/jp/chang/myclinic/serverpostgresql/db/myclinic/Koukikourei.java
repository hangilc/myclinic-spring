package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="koukikourei")
public class Koukikourei {
	@Id
	@Column(name="koukikourei_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer koukikoureiId;

	public Integer getKoukikoureiId(){
		return koukikoureiId;
	}

	public void setKoukikoureiId(Integer koukikoureiId){
		this.koukikoureiId = koukikoureiId;
	}

	@Column(name="patient_id")
	private Integer patientId;

	public Integer getPatientId(){
		return patientId;
	}

	public void setPatientId(Integer patientId){
		this.patientId = patientId;
	}

	@Column(name="hokensha_bangou")
	private Integer hokenshaBangou;

	public Integer getHokenshaBangou(){
		return hokenshaBangou;
	}

	public void setHokenshaBangou(Integer hokenshaBangou){
		this.hokenshaBangou = hokenshaBangou;
	}

	@Column(name="hihokensha_bangou")
	private Integer hihokenshaBangou;

	public Integer getHihokenshaBangou(){
		return hihokenshaBangou;
	}

	public void setHihokenshaBangou(Integer hihokenshaBangou){
		this.hihokenshaBangou = hihokenshaBangou;
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
		return "Koukikourei[koukikoureiId=" + koukikoureiId + ", " +
			"patientId=" + patientId + ", " +
			"hokenshaBangou=" + hokenshaBangou + ", " +
			"hihokenshaBangou=" + hihokenshaBangou + ", " +
			"futanWari=" + futanWari + ", " +
			"validFrom=" + validFrom + ", " +
			"validUpto=" + validUpto + 
			"]";
	}
}