package jp.chang.myclinic.server.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="hoken_koukikourei")
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