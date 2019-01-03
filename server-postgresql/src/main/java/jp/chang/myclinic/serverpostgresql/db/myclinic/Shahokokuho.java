package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="shahokokuho")
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
	private LocalDate validFrom;

	@Column(name="valid_upto")
	private LocalDate validUpto;

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

	public LocalDate getValidFrom(){
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom){
		this.validFrom = validFrom;
	}

	public LocalDate getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(LocalDate validUpto){
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