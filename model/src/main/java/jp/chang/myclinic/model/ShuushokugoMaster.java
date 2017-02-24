package jp.chang.myclinic.model;

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
@Table(name="shuushokugo_master")
public class ShuushokugoMaster {

	@Id
	private Integer shuushokugocode;

	public Integer getShuushokugocode(){
		return shuushokugocode;
	}

	public void setShuushokugocode(Integer shuushokugocode){
		this.shuushokugocode = shuushokugocode;
	}

	private String name;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	@Override
	public String toString(){
		return "ShuushokugoMaster[" +
			"shuushokugocode=" + shuushokugocode + ", " +
			"name=" + name +
		"]";
	}
}