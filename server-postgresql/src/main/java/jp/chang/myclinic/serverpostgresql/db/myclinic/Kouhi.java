package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.time.LocalDate;

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
		return "Kouhi[kouhiId=" + kouhiId + ", " +
			"patientId=" + patientId + ", " +
			"futansha=" + futansha + ", " +
			"jukyuusha=" + jukyuusha + ", " +
			"validFrom=" + validFrom + ", " +
			"validUpto=" + validUpto
		+ "]";
	}
}