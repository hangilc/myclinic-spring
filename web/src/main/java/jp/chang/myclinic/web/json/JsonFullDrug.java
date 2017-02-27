package jp.chang.myclinic.web.json;

import jp.chang.myclinic.model.Drug;
import jp.chang.myclinic.model.IyakuhinMaster;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.math.BigDecimal;

public class JsonFullDrug {

	@JsonProperty("drug_id")
	private Integer drugId;

	public Integer getDrugId(){
		return drugId;
	}

	public void setDrugId(Integer drugId){
		this.drugId = drugId;
	}

	@JsonProperty("visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	@JsonProperty("d_iyakuhincode")
	private Integer iyakuhincode;

	public Integer getIyakuhincode(){
		return iyakuhincode;
	}

	public void setIyakuhincode(Integer iyakuhincode){
		this.iyakuhincode = iyakuhincode;
	}

	@JsonProperty("d_amount")
	private BigDecimal amount;

	public BigDecimal getAmount(){
		return amount;
	}

	public void setAmount(BigDecimal amount){
		this.amount = amount;
	}

	@JsonProperty("d_usage")
	private String usage;

	public String getUsage(){
		return usage;
	}

	public void setUsage(String usage){
		this.usage = usage;
	}

	@JsonProperty("d_days")
	private Integer days;

	public Integer getDays(){
		return days;
	}

	public void setDays(Integer days){
		this.days = days;
	}

	@JsonProperty("d_category")
	private Integer category;

	public Integer getCategory(){
		return category;
	}

	public void setCategory(Integer category){
		this.category = category;
	}

	@JsonProperty("d_shuukeisaki")
	private Integer shuukeisaki;

	public Integer getShuukeisaki(){
		return shuukeisaki;
	}

	public void setShuukeisaki(Integer shuukeisaki){
		this.shuukeisaki = shuukeisaki;
	}

	@JsonProperty("d_prescribed")
	private Integer prescribed;

	public Integer getPrescribed(){
		return prescribed;
	}

	public void setPrescribed(Integer prescribed){
		this.prescribed = prescribed;
	}

	@JsonProperty("d_master_valid_from")
	private Date masterValidFrom;

	public Date getMasterValidFrom(){
		return masterValidFrom;
	}

	public void setMasterValidFrom(Date mastervalidfrom){
		this.masterValidFrom = masterValidFrom;
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

	public static JsonFullDrug create(Drug drug){
		JsonFullDrug dst = new JsonFullDrug();
		dst.setDrugId(drug.getDrugId());
		dst.setVisitId(drug.getVisitId());
		dst.setIyakuhincode(drug.getIyakuhincode());
		dst.setAmount(drug.getAmount());
		dst.setUsage(drug.getUsage());
		dst.setDays(drug.getDays());
		dst.setCategory(drug.getCategory());
		dst.setShuukeisaki(drug.getShuukeisaki());
		dst.setPrescribed(drug.getPrescribed());
		dst.setMasterValidFrom(drug.getMasterValidFrom());
		IyakuhinMaster m = drug.getMaster();
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