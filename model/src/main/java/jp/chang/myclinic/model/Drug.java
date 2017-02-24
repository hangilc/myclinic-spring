package jp.chang.myclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.FetchType;
import javax.persistence.Transient;
import java.sql.Date;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name="visit_drug")
public class Drug {

	@Id
	@GeneratedValue
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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="visit_id", insertable=false, updatable=false)
	private Visit visit;

	public Visit getVisit(){
		return visit;
	}

	public void setVisit(Visit visit){
		this.visit = visit;
	}

	@Column(name="d_iyakuhincode")
	private Integer iyakuhincode;

	public Integer getIyakuhincode(){
		return iyakuhincode;
	}

	public void setIyakuhincode(Integer iyakuhincode){
		this.iyakuhincode = iyakuhincode;
	}

	@Column(name="d_amount")
	private BigDecimal amount;

	public BigDecimal getAmount(){
		return amount;
	}

	public void setAmount(BigDecimal amount){
		this.amount = amount;
	}

	@Column(name="d_usage")
	private String usage;

	public String getUsage(){
		return usage;
	}

	public void setUsage(String usage){
		this.usage = usage;
	}

	@Column(name="d_days")
	private Integer days;

	public Integer getDays(){
		return days;
	}

	public void setDays(Integer days){
		this.days = days;
	}

	@Column(name="d_category")
	private Integer category;

	public Integer getCategory(){
		return category;
	}

	public void setCategory(Integer category){
		this.category = category;
	}

	@Column(name="d_shuukeisaki")
	private Integer shuukeisaki;

	public Integer getShuukeisaki(){
		return shuukeisaki;
	}

	public void setShuukeisaki(Integer shuukeisaki){
		this.shuukeisaki = shuukeisaki;
	}

	@Column(name="d_prescribed")
	private Integer prescribed;

	public Integer getPrescribed(){
		return prescribed;
	}

	public void setPrescribed(Integer prescribed){
		this.prescribed = prescribed;
	}

	@Column(name="d_master_valid_from")
	private Date masterValidFrom;

	public Date getMasterValidFrom(){
		return masterValidFrom;
	}

	public void setMasterValidFrom(Date mastervalidfrom){
		this.masterValidFrom = masterValidFrom;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="d_iyakuhincode", referencedColumnName="iyakuhincode", insertable=false, updatable=false),
		@JoinColumn(name="d_master_valid_from", referencedColumnName="valid_from", insertable=false, updatable=false),
	})
	private IyakuhinMaster master;

	public IyakuhinMaster getMaster(){
		return master;
	}

	public void setMaster(IyakuhinMaster master){
		this.master = master;
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
			"shuukeisaki=" + shuukeisaki + ", " +
			"prescribed=" + prescribed + ", " +
			"masterValidFrom=" + masterValidFrom +
		"]";
	}
}
