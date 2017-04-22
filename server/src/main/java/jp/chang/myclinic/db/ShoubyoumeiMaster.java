package jp.chang.myclinic.db;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name="shoubyoumei_master_arch")
@IdClass(ShoubyoumeiMasterId.class)
public class ShoubyoumeiMaster {

	@Id
	private Integer shoubyoumeicode;

	public Integer getShoubyoumeicode(){
		return shoubyoumeicode;
	}

	public void setShoubyoumeicode(Integer shoubyoumeicode){
		this.shoubyoumeicode = shoubyoumeicode;
	}

	@Id
	@Column(name="valid_from")
	private Date validFrom;

	public Date getValidFrom(){
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	private String name;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	private Date validUpto;

	public Date getValidUpto(){
		return validUpto;
	}

	public void setValidUpto(Date validUpto){
		this.validUpto = validUpto;
	}

	@Override
	public String toString(){
		return "ShoubyoumeiMaster[" +
			"shoubyoumeicode=" + shoubyoumeicode + ", " +
			"validFrom=" + validFrom + ", " +
			"name=" + name + ", " +
			"validUpto=" + validUpto +
		"]";
	}
}