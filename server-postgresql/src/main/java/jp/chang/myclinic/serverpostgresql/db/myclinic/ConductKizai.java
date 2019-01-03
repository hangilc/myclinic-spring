package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="conduct_kizai")
public class ConductKizai {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="conduct_kizai_id")
	private Integer conductKizaiId;

	public Integer getConductKizaiId(){
		return conductKizaiId;
	}

	public void setConductKizaiId(Integer conductKizaiId){
		this.conductKizaiId = conductKizaiId;
	}

	@Column(name="conduct_id")
	private Integer conductId;

	public Integer getConductId(){
		return conductId;
	}

	public void setConductId(Integer conductId){
		this.conductId = conductId;
	}

	private Integer kizaicode;

	public Integer getKizaicode(){
		return kizaicode;
	}

	public void setKizaicode(Integer kizaicode){
		this.kizaicode = kizaicode;
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
		return "ConductKizai[" +
			"conductKizaiId=" + conductKizaiId + ", " +
			"conductId=" + conductId + ", " +
			"kizaicode=" + kizaicode + ", " +
			"amount=" + amount +
		"]";
	}
}
