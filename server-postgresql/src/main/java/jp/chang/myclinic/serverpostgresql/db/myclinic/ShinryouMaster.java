package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="shinryou_master")
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

	private BigDecimal tensuu;

	public BigDecimal getTensuu(){
		return tensuu;
	}

	public void setTensuu(BigDecimal tensuu){
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

	private String kensagroup;

	public String getKensagroup(){
		return kensagroup;
	}

	public void setKensagroup(String kensagroup){
		this.kensagroup = kensagroup;
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
		return "ShinryouMaster[" +
			"shinryoucode=" + shinryoucode + ", " +
			"validFrom=" + validFrom + ", " +
			"name=" + name + ", " +
			"tensuu=" + tensuu + ", " +
			"tensuuShikibetsu=" + tensuuShikibetsu + ", " +
			"shuukeisaki=" + shuukeisaki + ", " +
			"houkatsukensa=" + houkatsukensa + ", " +
			"kensagroup=" + kensagroup + ", " +
			"validUpto=" + validUpto +
		"]";
	}
}