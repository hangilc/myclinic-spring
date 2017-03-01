package jp.chang.myclinic.web.service.json;

import jp.chang.myclinic.model.ConductDrug;
import jp.chang.myclinic.model.IyakuhinMaster;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.math.BigDecimal;

public class JsonFullConductDrug {

	@JsonProperty("id")
	private Integer conductDrugId;

	public Integer getConductDrugId(){
		return conductDrugId;
	}

	public void setConductDrugId(Integer conductDrugId){
		this.conductDrugId = conductDrugId;
	}

	@JsonProperty("visit_conduct_id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	private Integer iyakuhincode;

	public Integer getIyakuhincode(){
		return iyakuhincode;
	}

	public void setIyakuhincode(Integer iyakuhincode){
		this.iyakuhincode = iyakuhincode;
	}

	@JsonProperty("master_valid_from")
	private Date masterValidFrom;

	public Date getMasterValidFrom(){
		return masterValidFrom;
	}

	public void setMasterValidFrom(Date masterValidFrom){
		this.masterValidFrom = masterValidFrom;
	}

	private BigDecimal amount;

	public BigDecimal getAmount(){
		return amount;
	}

	public void setAmount(BigDecimal amount){
		this.amount = amount;
	}

	@JsonProperty("valid_from")
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

	@JsonProperty("valid_upto")
	private Date validUpto;

	public Date getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(Date validUpto){
		this.validUpto = validUpto;
	}

	public static JsonFullConductDrug create(ConductDrug src){
		JsonFullConductDrug dst = new JsonFullConductDrug();
		dst.setConductDrugId(src.getConductDrugId());
		dst.setConductId(src.getConductId());
		dst.setIyakuhincode(src.getIyakuhincode());
		dst.setAmount(src.getAmount());
		dst.setMasterValidFrom(src.getMasterValidFrom());
		IyakuhinMaster m = src.getMaster();
		dst.setValidFrom(m.getValidFrom());
		dst.setName(m.getName());
		dst.setYomi(m.getYomi());
		dst.setUnit(m.getUnit());
		dst.setYakka(m.getYakka());
		dst.setMadoku(m.getMadoku());
		dst.setKouhatsu(m.getKouhatsu());
		dst.setZaikei(m.getZaikei());
		dst.setValidUpto(m.getValidUpto());
		return dst;
	}
}