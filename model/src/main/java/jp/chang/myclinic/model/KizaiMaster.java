package jp.chang.myclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

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
	private Date validUpto;

	public Date getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(Date validUpto){
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