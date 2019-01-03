package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="kizai_master")
@IdClass(KizaiMasterId.class)
public class KizaiMaster {
	
	@Id
	private Integer kizaicode;

	public Integer getKizaicode(){
		return kizaicode;
	}

	public void setKizaicode(Integer kizaicode){
		this.kizaicode = kizaicode;
	}

	@Id
	@Column(name="valid_from")
	private LocalDate validFrom;

	public LocalDate getValidFrom(){
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom){
		this.validFrom = validFrom;
	}

	private String name;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	private String yomi;

	public String getYomi(){
		return yomi;
	}

	public void setYomi(String yomi){
		this.yomi = yomi;
	}

	private String unit;

	public String getUnit(){
		return unit;
	}

	public void setUnit(String unit){
		this.unit = unit;
	}

	private BigDecimal kingaku;

	public BigDecimal getKingaku(){
		return kingaku;
	}

	public void setKingaku(BigDecimal kingaku){
		this.kingaku = kingaku;
	}

	@Column(name = "valid_upto")
	private LocalDate validUpto;

	public LocalDate getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(LocalDate validUpto){
		this.validUpto = validUpto;
	}

	@Override
	public String toString(){
		return "KizaiMaster[" +
			"kizaicode=" + kizaicode + ", " +
			"validFrom=" + validFrom + ", " +
			"name=" + name + ", " +
			"yomi=" + yomi + ", " +
			"unit=" + unit + ", " +
			"kingaku=" + kingaku + ", " +
			"validUpto=" + validUpto +
		"]";
	}
}