package jp.chang.myclinic.dbmysql.core;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="visit_conduct_drug")
public class ConductDrug {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	private Integer conductDrugId;

	public Integer getConductDrugId(){
		return conductDrugId;
	}

	public void setConductDrugId(Integer conductDrugId){
		this.conductDrugId = conductDrugId;
	}

	@Column(name="visit_conduct_id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="visit_conduct_id", insertable=false, updatable=false)
	private Conduct conduct;

	public Conduct getConduct(){
		return conduct;
	}

	public void setConduct(Conduct conduct){
		this.conduct = conduct;
	}

	private Integer iyakuhincode;

	public Integer getIyakuhincode(){
		return iyakuhincode;
	}

	public void setIyakuhincode(Integer iyakuhincode){
		this.iyakuhincode = iyakuhincode;
	}

	// @Column(name="master_valid_from")
	// private Date masterValidFrom;

	// public Date getMasterValidFrom(){
	// 	return masterValidFrom;
	// }

	// public void setMasterValidFrom(Date masterValidFrom){
	// 	this.masterValidFrom = masterValidFrom;
	// }

	// @ManyToOne(fetch=FetchType.LAZY)
	// @JoinColumns({
	// 	@JoinColumn(name="iyakuhincode", referencedColumnName="iyakuhincode", insertable=false, updatable=false),
	// 	@JoinColumn(name="master_valid_from", referencedColumnName="valid_from", insertable=false, updatable=false),
	// })
	// private IyakuhinMaster master;

	// public IyakuhinMaster getMaster(){
	// 	return master;
	// }

	// public void setMaster(IyakuhinMaster master){
	// 	this.master = master;
	// }

	private BigDecimal amount;

	public BigDecimal getAmount(){
		return amount;
	}

	public void setAmount(BigDecimal amount){
		this.amount = amount;
	}

	@Override
	public String toString(){
		return "ConductDrug[" +
			"conductDrugId=" + conductDrugId + ", " +
			"conductId=" + conductId + ", " +
			"iyakuhincode=" + iyakuhincode + ", " +
			"amount=" + amount + //", " +
			//"masterValidFrom=" + masterValidFrom +
		"]";
	}
}
