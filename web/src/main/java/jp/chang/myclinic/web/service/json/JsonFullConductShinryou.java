package jp.chang.myclinic.web.service.json;

import jp.chang.myclinic.model.ConductShinryou;
import jp.chang.myclinic.model.ShinryouMaster;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.math.BigDecimal;

public class JsonFullConductShinryou {

	@JsonProperty("id")
	private Integer conductShinryouId;

	public Integer getConductShinryouId(){
		return conductShinryouId;
	}

	public void setConductShinryouId(Integer conductShinryouId){
		this.conductShinryouId = conductShinryouId;
	}

	@JsonProperty("visit_conduct_id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	private Integer shinryoucode;

	public Integer getShinryoucode(){
		return shinryoucode;
	}

	public void setShinryoucode(Integer shinryoucode){
		this.shinryoucode = shinryoucode;
	}

	@JsonProperty("master_valid_from")
	private Date masterValidFrom;

	public Date getMasterValidFrom(){
		return masterValidFrom;
	}

	public void setMasterValidFrom(Date masterValidFrom){
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

	private Integer tensuu;

	public Integer getTensuu(){
		return tensuu;
	}

	public void setTensuu(Integer tensuu){
		this.tensuu = tensuu;
	}

	@JsonProperty("tensuu_shikibetsu")
	private Character tensuuShikibetsu;

	public Character getTensuuShikibetsu(){
		return tensuuShikibetsu;
	}

	public void setTensuuShikibetsu(Character tensuuShikibetsu){
		this.tensuuShikibetsu = tensuuShikibetsu;
	}

	private String shuukeisaki;

	public String getShuukeisaki(){
		return shuukeisaki;
	}

	public void setShuukeisaki(String shuukeisaki){
		this.shuukeisaki = shuukeisaki;
	}

	private String houkatsukensa;

	public String getHoukatsukensa(){
		return houkatsukensa;
	}

	public void setHoukatsukensa(String houkatsukensa){
		this.houkatsukensa = houkatsukensa;
	}

	private Character oushinkubun;

	public Character getOushinkubun(){
		return oushinkubun;
	}

	public void setOushinkubun(Character oushinkubun){
		this.oushinkubun = oushinkubun;
	}

	private String kensagroup;

	public String getKensagroup(){
		return kensagroup;
	}

	public void setKensagroup(String kensagroup){
		this.kensagroup = kensagroup;
	}

	private Character roujintekiyou;

	public Character getRoujintekiyou(){
		return roujintekiyou;
	}

	public void setRoujintekiyou(Character roujintekiyou){
		this.roujintekiyou = roujintekiyou;
	}

	@JsonProperty("code_shou")
	private Character codeShou;

	public Character getCodeShou(){
		return codeShou;
	}

	public void setCodeShou(Character codeShou){
		this.codeShou = codeShou;
	}

	@JsonProperty("code_bu")
	private String codeBu;

	public String getCodeBu(){
		return codeBu;
	}

	public void setCodeBu(String codeBu){
		this.codeBu = codeBu;
	}

	@JsonProperty("code_alpha")
	private Character codeAlpha;

	public Character getCodeAlpha(){
		return codeAlpha;
	}

	public void setCodeAlpha(Character codeAlpha){
		this.codeAlpha = codeAlpha;
	}

	@JsonProperty("code_kubun")
	private String codeKubun;

	public String getCodeKubun(){
		return codeKubun;
	}

	public void setCodeKubun(String codeKubun){
		this.codeKubun = codeKubun;
	}

	@JsonProperty("valid_upto")
	private Date validUpto;

	public Date getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(Date validUpto){
		this.validUpto = validUpto;
	}

	public static JsonFullConductShinryou create(ConductShinryou src){
		JsonFullConductShinryou dst = new JsonFullConductShinryou();
		dst.setConductShinryouId(src.getConductShinryouId());
		dst.setConductId(src.getConductId());
		dst.setShinryoucode(src.getShinryoucode());
		dst.setMasterValidFrom(src.getMasterValidFrom());
		ShinryouMaster m = src.getMaster();
		dst.setValidFrom(m.getValidFrom());
		dst.setName(m.getName());
		dst.setTensuu(m.getTensuu());
		dst.setTensuuShikibetsu(m.getTensuuShikibetsu());
		dst.setShuukeisaki(m.getShuukeisaki());
		dst.setHoukatsukensa(m.getHoukatsukensa());
		dst.setOushinkubun(m.getOushinkubun());
		dst.setKensagroup(m.getKensagroup());
		dst.setRoujintekiyou(m.getRoujintekiyou());
		dst.setCodeShou(m.getCodeShou());
		dst.setCodeBu(m.getCodeBu());
		dst.setCodeAlpha(m.getCodeAlpha());
		dst.setCodeKubun(m.getCodeKubun());
		dst.setValidUpto(m.getValidUpto());
		return dst;
	}

}