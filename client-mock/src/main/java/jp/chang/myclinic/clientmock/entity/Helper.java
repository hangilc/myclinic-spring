package jp.chang.myclinic.clientmock.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Helper {

    boolean isValidAt(String validFrom, String validUpto, String at){
        if( at.length() > 10 ){
            at = at.substring(0, 10);
        }
        if( at.compareTo(validFrom) < 0 ){
            return false;
        }
        if( validUpto == null || validUpto.equals("0000-00-00") ){
            return true;
        } else {
            return at.compareTo(validUpto) <= 0;
        }
    }

}
