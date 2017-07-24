package jp.chang.myclinic.db.myclinic;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="kouhi")
public class Kouhi {
	@Id
	@Column(name="kouhi_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer kouhiId;

	public Integer getKouhiId(){
		return kouhiId;
	}

	public void setKouhiId(Integer kouhiId){
		this.kouhiId = kouhiId;
	}

	@Column(name="patient_id")
	private Integer patientId;

	public Integer getPatientId(){
		return patientId;
	}

	public void setPatientId(Integer patientId){
		this.patientId = patientId;
	}

	private Integer futansha;

	public Integer getFutansha(){
		return futansha;
	}

	public void setFutansha(Integer futansha){
		this.futansha = futansha;
	}

	private Integer jukyuusha;

	public Integer getJukyuusha(){
		return jukyuusha;
	}

	public void setJukyuusha(Integer jukyuusha){
		this.jukyuusha = jukyuusha;
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
	private String validUpto;

	public String getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(String validUpto){
		this.validUpto = validUpto;
	}

	@Override
	public String toString(){
		return "Kouhi[kouhiId=" + kouhiId + ", " +
			"patientId=" + patientId + ", " +
			"futansha=" + futansha + ", " +
			"jukyuusha=" + jukyuusha + ", " +
			"validFrom=" + validFrom + ", " +
			"validUpto=" + validUpto
		+ "]";
	}
}