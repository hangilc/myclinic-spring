package jp.chang.myclinic.backendmysql.entity.core;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="shinryoukoui_master_arch")
@IdClass(ShinryouMasterId.class)
public class ShinryouMaster {

	@Id
	private Integer shinryoucode;

	public Integer getShinryoucode(){
		return shinryoucode;
	}

	public void setShinryoucode(Integer shinryoucode){
		this.shinryoucode = shinryoucode;
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

	private Integer tensuu;

	public Integer getTensuu(){
		return tensuu;
	}

	public void setTensuu(Integer tensuu){
		this.tensuu = tensuu;
	}

	@Column(name="tensuu_shikibetsu")
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

	//private Character roujintekiyou;

//	public Character getRoujintekiyou(){
//		return roujintekiyou;
//	}
//
//	public void setRoujintekiyou(Character roujintekiyou){
//		this.roujintekiyou = roujintekiyou;
//	}

//	@Column(name="code_shou")
//	private Character codeShou;
//
//	public Character getCodeShou(){
//		return codeShou;
//	}
//
//	public void setCodeShou(Character codeShou){
//		this.codeShou = codeShou;
//	}

//	@Column(name="code_bu")
//	private String codeBu;
//
//	public String getCodeBu(){
//		return codeBu;
//	}
//
//	public void setCodeBu(String codeBu){
//		this.codeBu = codeBu;
//	}

//	@Column(name="code_alpha")
//	private Character codeAlpha;
//
//	public Character getCodeAlpha(){
//		return codeAlpha;
//	}
//
//	public void setCodeAlpha(Character codeAlpha){
//		this.codeAlpha = codeAlpha;
//	}

//	@Column(name="code_kubun")
//	private String codeKubun;
//
//	public String getCodeKubun(){
//		return codeKubun;
//	}
//
//	public void setCodeKubun(String codeKubun){
//		this.codeKubun = codeKubun;
//	}

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
		return "ShinryouMaster[" +
			"shinryoucode=" + shinryoucode + ", " +
			"validFrom=" + validFrom + ", " +
			"name=" + name + ", " +
			"tensuu=" + tensuu + ", " +
			"tensuuShikibetsu=" + tensuuShikibetsu + ", " +
			"shuukeisaki=" + shuukeisaki + ", " +
			"houkatsukensa=" + houkatsukensa + ", " +
			"oushinkubun=" + oushinkubun + ", " +
			"kensagroup=" + kensagroup + ", " +
			//"roujintekiyou=" + roujintekiyou + ", " +
//			"codeShou=" + codeShou + ", " +
//			"codeBu=" + codeBu + ", " +
//			"codeAlpha=" + codeAlpha + ", " +
//			"codeKubun=" + codeKubun + ", " +
			"validUpto=" + validUpto +
		"]";
	}
}