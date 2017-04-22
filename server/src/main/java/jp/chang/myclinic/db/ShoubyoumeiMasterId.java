package jp.chang.myclinic.db;

import java.sql.Date;
import java.io.Serializable;
import java.util.Objects;

public class ShoubyoumeiMasterId implements Serializable {
	public Integer shoubyoumeicode;
	public Date validFrom;

	public ShoubyoumeiMasterId(){

	}

	public ShoubyoumeiMasterId(Integer shoubyoumeicode, Date validFrom){
		this.shoubyoumeicode = shoubyoumeicode;
		this.validFrom = validFrom;
	}

	public Integer getShoubyoumeicode(){
		return shoubyoumeicode;
	}

	public Date getValidFrom(){
		return validFrom;
	}

	@Override
	public int hashCode(){
		return Objects.hash(this.shoubyoumeicode, this.validFrom);
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
		ShoubyoumeiMasterId obj = (ShoubyoumeiMasterId)o;
		return Objects.equals(this.shoubyoumeicode, obj.shoubyoumeicode) && Objects.equals(this.validFrom, obj.validFrom);
	}
}