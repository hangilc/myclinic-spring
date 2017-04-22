package jp.chang.myclinic.db;

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
import java.sql.Timestamp;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name="pharma_drug")
public class PharmaDrug {

	@Id
	private Integer iyakuhincode;

	public Integer getIyakuhincode(){
		return iyakuhincode;
	}

	public void setIyakuhincode(Integer iyakuhincode){
		this.iyakuhincode = iyakuhincode;
	}

	private String description;

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	@Column(name="sideeffect")
	private String sideEffect;

	public String getSideEffect(){
		return sideEffect;
	}

	public void setSideEffect(String sideEffect){
		this.sideEffect = sideEffect;
	}

	@Override
	public String toString(){
		return "PharmaDrug[" +
			"iyakuhincode=" + iyakuhincode + ", " +
			"description=" + description + ", " +
			"sideEffect=" + sideEffect +
		"]";
	}

}
