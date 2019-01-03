package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="iyakuhin_master")
@IdClass(IyakuhinMasterId.class)
public class IyakuhinMaster {
	@Id
	private Integer iyakuhincode;

	public Integer getIyakuhincode(){
		return iyakuhincode;
	}

	public void setIyakuhincode(Integer iyakuhincode){
		this.iyakuhincode = iyakuhincode;
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

	private BigDecimal yakka;

	public BigDecimal getYakka(){
		return yakka;
	}

	public void setYakka(BigDecimal yakka){
		this.yakka = yakka;
	}

	private Character madoku;

	public Character getMadoku(){
		return madoku;
	}

	public void setMadoku(Character madoku){
		this.madoku = madoku;
	}

	private Character kouhatsu;

	public Character getKouhatsu(){
		return kouhatsu;
	}

	public void setKouhatsu(Character kouhatsu){
		this.kouhatsu = kouhatsu;
	}

	private Character zaikei;

	public Character getZaikei(){
		return zaikei;
	}

	public void setZaikei(Character zaikei){
		this.zaikei = zaikei;
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
		return "IyakuhinMaster[" +
			"iyakuhincode=" + iyakuhincode + ", " +
			"validFrom=" + validFrom + ", " +
			"name=" + name + ", " +
			"yomi=" + yomi + ", " +
			"unit=" + unit + ", " +
			"yakka=" + yakka + ", " +
			"madoku=" + madoku + ", " +
			"kouhatsu=" + kouhatsu + ", " +
			"zaikei=" + zaikei + ", " +
			"validUpto=" + validUpto +
		"]";
	}
}