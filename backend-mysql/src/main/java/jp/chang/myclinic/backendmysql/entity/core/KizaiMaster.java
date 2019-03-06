package jp.chang.myclinic.backendmysql.entity.core;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name="tokuteikizai_master_arch")
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
	private Date validFrom;

	public Date getValidFrom(){
		return validFrom;
	}

	public void setValidFrom(Date validFrom){
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
	private String validUpto;

	public String getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(String validUpto){
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