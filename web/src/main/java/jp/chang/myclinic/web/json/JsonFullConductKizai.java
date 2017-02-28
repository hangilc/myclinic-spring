package jp.chang.myclinic.web.json;

import jp.chang.myclinic.model.ConductKizai;
import jp.chang.myclinic.model.KizaiMaster;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.math.BigDecimal;

public class JsonFullConductKizai {

	@JsonProperty("id")
	private Integer conductKizaiId;

	public Integer getConductKizaiId(){
		return conductKizaiId;
	}

	public void setConductKizaiId(Integer conductKizaiId){
		this.conductKizaiId = conductKizaiId;
	}

	@JsonProperty("visit_conduct_id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	private Integer kizaicode;

	public Integer getKizaicode(){
		return kizaicode;
	}

	public void setKizaicode(Integer kizaicode){
		this.kizaicode = kizaicode;
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

	private BigDecimal kingaku;

	public BigDecimal getKingaku(){
		return kingaku;
	}

	public void setKingaku(BigDecimal kingaku){
		this.kingaku = kingaku;
	}

	@JsonProperty("valid_upto")
	private Date validUpto;

	public Date getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(Date validUpto){
		this.validUpto = validUpto;
	}

	public static JsonFullConductKizai create(ConductKizai src){
		JsonFullConductKizai dst = new JsonFullConductKizai();
		dst.setConductKizaiId(src.getConductKizaiId());
		dst.setConductId(src.getConductId());
		dst.setKizaicode(src.getKizaicode());
		dst.setAmount(src.getAmount());
		dst.setMasterValidFrom(src.getMasterValidFrom());
		KizaiMaster m = src.getMaster();
		dst.setValidFrom(m.getValidFrom());
		dst.setName(m.getName());
		dst.setYomi(m.getYomi());
		dst.setUnit(m.getUnit());
		dst.setKingaku(m.getKingaku());
		dst.setValidUpto(m.getValidUpto());
		return dst;
	}
}