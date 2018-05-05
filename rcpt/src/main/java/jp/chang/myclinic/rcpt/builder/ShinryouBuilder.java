package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.ShinryouDTO;

public class ShinryouBuilder {

    private ShinryouDTO shinryou = new ShinryouDTO();

    ShinryouBuilder() {

    }

    public ShinryouDTO build(){
        return shinryou;
    }

    public ShinryouBuilder setShinryouId(int shinryouId){
        shinryou.shinryouId = shinryouId;
        return this;
    }

    public ShinryouBuilder setShinryoucode(int shinryoucode){
        shinryou.shinryoucode = shinryoucode;
        return this;
    }

    public ShinryouBuilder setVisitId(int visitId){
        shinryou.visitId = visitId;
        return this;
    }

}
