package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.ShinryouMasterDTO;

import java.time.LocalDate;

public class ShinryouMasterModifier {

    private ShinryouMasterDTO value;

    ShinryouMasterModifier(ShinryouMasterDTO value) {
        this.value = value;
    }

    public void setValidFrom(String date){
        value.validFrom = LocalDate.parse(date).toString();
    }

    public void setValidUpto(String date){
        if( "0000-00-00".equals(date) ){
            value.validUpto = date;
        } else {
            value.validUpto = LocalDate.parse(date).toString();
        }
    }

}
