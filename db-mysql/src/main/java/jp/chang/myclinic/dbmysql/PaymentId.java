package jp.chang.myclinic.dbmysql;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class PaymentId implements Serializable {
	public Integer visitId;
	public Timestamp paytime;

	public PaymentId(){ }

	public PaymentId(Integer visitId, Timestamp paytime){
		this.visitId = visitId;
		this.paytime = paytime;
	}

	public Integer getVisitId(){
		return visitId;
	}

	public void setVisitId(Integer visitId){
		this.visitId = visitId;
	}

	public Timestamp getPaytime(){
		return paytime;
	}

	public void setPaytime(Timestamp paytime){
		this.paytime = paytime;
	}

	@Override
	public int hashCode(){
		return Objects.hash(this.visitId, this.paytime);
	}

	@Override
	public boolean equals(Object o){
		if( this == o ){
			return true;
		}
		if( o == null ){
			return false;
		}
		if( getClass() != o.getClass() ){
			return false;
		}
		PaymentId obj = (PaymentId)o;
		return Objects.equals(this.visitId, obj.visitId) && Objects.equals(this.paytime, obj.paytime);
	}


}