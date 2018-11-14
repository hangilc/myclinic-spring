package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="drug")
public class Drug {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="drug_id")
	private Integer drugId;

	public Integer getDrugId(){
		return drugId;
	}

	public void setDrugId(Integer drugId){
		this.drugId = drugId;
	}

	@Column(name="visit_id")
	private Integer visitId;

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	@Column(name="iyakuhincode")
	private Integer iyakuhincode;

	public Integer getIyakuhincode(){
		return iyakuhincode;
	}

	public void setIyakuhincode(Integer iyakuhincode){
		this.iyakuhincode = iyakuhincode;
	}

	@Column(name="amount")
	private BigDecimal amount;

	public BigDecimal getAmount(){
		return amount;
	}

	public void setAmount(BigDecimal amount){
		this.amount = amount;
	}

	@Column(name="usage")
	private String usage;

	public String getUsage(){
		return usage;
	}

	public void setUsage(String usage){
		this.usage = usage;
	}

	@Column(name="days")
	private Integer days;

	public Integer getDays(){
		return days;
	}

	public void setDays(Integer days){
		this.days = days;
	}

	@Column(name="category")
	private Integer category;

	public Integer getCategory(){
		return category;
	}

	public void setCategory(Integer category){
		this.category = category;
	}

	@Column(name="prescribed")
	private Integer prescribed;

	public Integer getPrescribed(){
		return prescribed;
	}

	public void setPrescribed(Integer prescribed){
		this.prescribed = prescribed;
	}

	@Override
	public String toString(){
		return "Drug[" +
			"drugId=" + drugId + ", " +
			"visitId=" + visitId + ", " +
			"iyakuhincode=" + iyakuhincode + ", " +
			"amount=" + amount + ", " +
			"usage=" + usage + ", " +
			"days=" + days + ", " +
			"category=" + category + ", " +
			"prescribed=" + prescribed +
		"]";
	}
}
