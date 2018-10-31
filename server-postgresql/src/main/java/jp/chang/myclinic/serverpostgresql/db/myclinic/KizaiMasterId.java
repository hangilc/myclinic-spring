package jp.chang.myclinic.serverpostgresql.db.myclinic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class KizaiMasterId implements Serializable {
	public Integer kizaicode;
	public LocalDate validFrom;

	public KizaiMasterId(){

	}

	public KizaiMasterId(Integer kizaicode, LocalDate validFrom){
		this.kizaicode = kizaicode;
		this.validFrom = validFrom;
	}

	public Integer getKizaicode(){
		return kizaicode;
	}

	public LocalDate getValidFrom(){
		return validFrom;
	}

	@Override
	public int hashCode(){
		return Objects.hash(this.kizaicode, this.validFrom);
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
		KizaiMasterId obj = (KizaiMasterId)o;
		return Objects.equals(this.kizaicode, obj.kizaicode) && Objects.equals(this.validFrom, obj.validFrom);
	}
}