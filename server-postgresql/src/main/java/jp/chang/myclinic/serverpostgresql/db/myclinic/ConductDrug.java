package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="conduct_drug")
public class ConductDrug {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="conduct_drug_id")
	private Integer conductDrugId;

	public Integer getConductDrugId(){
		return conductDrugId;
	}

	public void setConductDrugId(Integer conductDrugId){
		this.conductDrugId = conductDrugId;
	}

	@Column(name="conduct_id")
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
			"amount=" + amount +
		"]";
	}
}
