package jp.chang.myclinic.dbmysql;

import javax.persistence.*;

@Entity
@Table(name="hoken_shahokokuho")
public class Shahokokuho {
	@Id
	@Column(name="shahokokuho_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer shahokokuhoId;

	@Column(name="patient_id")
	private Integer patientId;

	@Column(name="hokensha_bangou")
	private Integer hokenshaBangou;

	@Column(name="hihokensha_kigou")
	private String hihokenshaKigou;

	@Column(name="hihokensha_bangou")
	private String hihokenshaBangou;

	private Integer honnin;

	private Integer kourei;

	@Column(name="valid_from")
	private String validFrom;

	@Column(name="valid_upto")
	private String validUpto;

	public Integer getShahokokuhoId(){
		return shahokokuhoId;
	}

	public void setShahokokuhoId(Integer shahokokuhoId){
		this.shahokokuhoId = shahokokuhoId;
	}

	public Integer getPatientId(){
		return patientId;
	}

	public void setPatientId(Integer patientId){
		this.patientId = patientId;
	}

	public Integer getHokenshaBangou(){
		return hokenshaBangou;
	}

	public void setHokenshaBangou(Integer hokenshaBangou){
		this.hokenshaBangou = hokenshaBangou;
	}

	public String getHihokenshaKigou(){
		return hihokenshaKigou;
	}

	public void setHihokenshaKigou(String hihokenshaKigou){
		this.hihokenshaKigou = hihokenshaKigou;
	}

	public String getHihokenshaBangou(){
		return hihokenshaBangou;
	}

	public void setHihokenshaBangou(String hihokenshaBangou){
		this.hihokenshaBangou = hihokenshaBangou;
	}

	public Integer getHonnin(){
		return honnin;
	}

	public void setHonnin(Integer honnin){
		this.honnin = honnin;
	}

	public Integer getKourei(){
		return kourei;
	}

	public void setKourei(Integer kourei){
		this.kourei = kourei;
	}

	public String getValidFrom(){
		return validFrom;
	}

	public void setValidFrom(String validFrom){
		this.validFrom = validFrom;
	}

	public String getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(String validUpto){
		this.validUpto = validUpto;
	}

	@Override
	public String toString(){
		return "Shahokokuho[shahokokuhoId=" + shahokokuhoId + ", " +
			"patientId=" + patientId + ", " +
			"hokenshaBangou=" + hokenshaBangou + ", " +
			"hihokenshaKigou=" + hihokenshaKigou + ", " +
			"hihokenshaBangou=" + hihokenshaBangou + ", " +
			"honnin=" + honnin + ", " +
			"kourei=" + kourei + ", " +
			"validFrom=" + validFrom + ", " +
			"validUpto=" + validUpto + 
			"]";
	}
}