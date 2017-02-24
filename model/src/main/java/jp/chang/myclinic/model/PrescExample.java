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
@Table(name="presc_example")
public class PrescExample {

	@Id
	@GeneratedValue
	@Column(name="presc_example_id")
	private Integer prescExampleId;

	public Integer getPrescExampleId(){
		return prescExampleId;
	}

	public void setPrescExampleId(Integer prescExampleId){
		this.prescExampleId = prescExampleId;
	}

	@Column(name="m_iyakuhincode")
	private Integer iyakuhincode;

	public Integer getIyakuhincode(){
		return iyakuhincode;
	}

	public void setIyakuhincode(Integer iyakuhincode){
		this.iyakuhincode = iyakuhincode;
	}

	@Column(name="m_amount")
	private BigDecimal amount;

	public BigDecimal getAmount(){
		return amount;
	}

	public void setAmount(BigDecimal amount){
		this.amount = amount;
	}

	@Column(name="m_usage")
	private String usage;

	public String getUsage(){
		return usage;
	}

	public void setUsage(String usage){
		this.usage = usage;
	}

	@Column(name="m_days")
	private Integer days;

	public Integer getDays(){
		return days;
	}

	public void setDays(Integer days){
		this.days = days;
	}

	@Column(name="m_category")
	private Integer category;

	public Integer getCategory(){
		return category;
	}

	public void setCategory(Integer category){
		this.category = category;
	}

	@Column(name="m_comment")
	private String comment;

	public String getComment(){
		return comment;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	@Column(name="m_master_valid_from")
	private Date masterValidFrom;

	public Date getMasterValidFrom(){
		return masterValidFrom;
	}

	public void setMasterValidFrom(Date mastervalidfrom){
		this.masterValidFrom = masterValidFrom;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="m_iyakuhincode", referencedColumnName="iyakuhincode", insertable=false, updatable=false),
		@JoinColumn(name="m_master_valid_from", referencedColumnName="valid_from", insertable=false, updatable=false),
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
			"prescExampleId=" + prescExampleId + ", " +
			"iyakuhincode=" + iyakuhincode + ", " +
			"amount=" + amount + ", " +
			"usage=" + usage + ", " +
			"days=" + days + ", " +
			"category=" + category + ", " +
			"comment=" + comment + ", " +
			"masterValidFrom=" + masterValidFrom +
		"]";
	}
}
