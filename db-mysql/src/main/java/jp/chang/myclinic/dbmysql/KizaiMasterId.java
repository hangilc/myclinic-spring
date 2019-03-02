package jp.chang.myclinic.dbmysql;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class KizaiMasterId implements Serializable {
	public Integer kizaicode;
	public Date validFrom;

	public KizaiMasterId(){

	}

	public KizaiMasterId(Integer kizaicode, Date validFrom){
		this.kizaicode = kizaicode;
		this.validFrom = validFrom;
	}

	public Integer getKizaicode(){
		return kizaicode;
	}

	public Date getValidFrom(){
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