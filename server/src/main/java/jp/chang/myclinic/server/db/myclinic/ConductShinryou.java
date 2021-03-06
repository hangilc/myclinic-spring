package jp.chang.myclinic.server.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="visit_conduct_shinryou")
public class ConductShinryou {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	private Integer conductShinryouId;

	public Integer getConductShinryouId(){
		return conductShinryouId;
	}

	public void setConductShinryouId(Integer conductShinryouId){
		this.conductShinryouId = conductShinryouId;
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

	private Integer shinryoucode;

	public Integer getShinryoucode(){
		return shinryoucode;
	}

	public void setShinryoucode(Integer shinryoucode){
		this.shinryoucode = shinryoucode;
	}

//	@Column(name="master_valid_from")
//	private Date masterValidFrom;
//
//	public Date getMasterValidFrom(){
//		return masterValidFrom;
//	}
//
//	public void setMasterValidFrom(Date masterValidFrom){
//		this.masterValidFrom = masterValidFrom;
//	}
//
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumns({
//		@JoinColumn(name="shinryoucode", referencedColumnName="shinryoucode", insertable=false, updatable=false),
//		@JoinColumn(name="master_valid_from", referencedColumnName="valid_from", insertable=false, updatable=false),
//	})
//	private ShinryouMaster master;
//
//	public ShinryouMaster getMaster(){
//		return master;
//	}
//
//	public void setMaster(ShinryouMaster master){
//		this.master = master;
//	}

	@Override
	public String toString(){
		return "ConductShinryou[" +
			"conductShinryouId=" + conductShinryouId + ", " +
			"conductId=" + conductId + ", " +
			"shinryoucode=" + shinryoucode + //", " +
			//"masterValidFrom=" + masterValidFrom +
		"]";
	}
}
