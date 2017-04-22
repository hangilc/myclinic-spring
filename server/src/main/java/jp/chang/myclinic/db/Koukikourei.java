package jp.chang.myclinic.db;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import java.sql.Date;

@Entity
@Table(name="hoken_koukikourei")
public class Koukikourei {
	@Id
	@Column(name="koukikourei_id")
	@GeneratedValue
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
	private String hokenshaBangou;

	public String getHokenshaBangou(){
		return hokenshaBangou;
	}

	public void setHokenshaBangou(String hokenshaBangou){
		this.hokenshaBangou = hokenshaBangou;
	}

	@Column(name="hihokensha_bangou")
	private String hihokenshaBangou;

	public String getHihokenshaBangou(){
		return hihokenshaBangou;
	}

	public void setHihokenshaBangou(String hihokenshaBangou){
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
	private Date validFrom;

	public Date getValidFrom(){
		return validFrom;
	}

	public void setValidFrom(Date validFrom){
		this.validFrom = validFrom;
	}

	@Column(name="valid_upto")
	private Date validUpto;

	public Date getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(Date validUpto){
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