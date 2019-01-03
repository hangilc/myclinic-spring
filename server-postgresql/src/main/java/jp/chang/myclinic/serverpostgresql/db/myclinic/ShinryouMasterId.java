package jp.chang.myclinic.serverpostgresql.db.myclinic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ShinryouMasterId implements Serializable {
	public Integer shinryoucode;
	public LocalDate validFrom;

	public ShinryouMasterId(){

	}

	public ShinryouMasterId(Integer shinryoucode, LocalDate validFrom){
		this.shinryoucode = shinryoucode;
		this.validFrom = validFrom;
	}

	public Integer getShinryoucode(){
		return shinryoucode;
	}

	public LocalDate getValidFrom(){
		return validFrom;
	}

	@Override
	public int hashCode(){
		return Objects.hash(this.shinryoucode, this.validFrom);
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
		ShinryouMasterId obj = (ShinryouMasterId)o;
		return Objects.equals(this.shinryoucode, obj.shinryoucode) && Objects.equals(this.validFrom, obj.validFrom);
	}
}