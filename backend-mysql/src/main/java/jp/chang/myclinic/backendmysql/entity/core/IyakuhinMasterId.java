package jp.chang.myclinic.backendmysql.entity.core;

import java.io.Serializable;
import java.util.Objects;

public class IyakuhinMasterId implements Serializable {
	public Integer iyakuhincode;
	public String validFrom;

	public IyakuhinMasterId(){

	}

	public IyakuhinMasterId(Integer iyakuhincode, String validFrom){
		this.iyakuhincode = iyakuhincode;
		this.validFrom = validFrom;
	}

	public Integer getIyakuhincode(){
		return iyakuhincode;
	}

	public String getValidFrom(){
		return validFrom;
	}

	@Override
	public int hashCode(){
		return Objects.hash(this.iyakuhincode, this.validFrom);
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
		IyakuhinMasterId obj = (IyakuhinMasterId)o;
		return Objects.equals(this.iyakuhincode, obj.iyakuhincode) && Objects.equals(this.validFrom, obj.validFrom);
	}
}