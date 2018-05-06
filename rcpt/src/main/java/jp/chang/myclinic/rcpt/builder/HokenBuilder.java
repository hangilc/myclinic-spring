package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.HokenDTO;

public class HokenBuilder {

    //private static Logger logger = LoggerFactory.getLogger(HokenBuilder.class);
    private HokenDTO result;

    public HokenBuilder() {
        result = new HokenDTO();
    }

    public HokenBuilder(HokenDTO value){
        this.result = value;
    }

    public HokenDTO build(){
        return result;
    }

    public HokenBuilder setPatientId(int patientId){
        if( result.shahokokuho != null ){
            result.shahokokuho.patientId = patientId;
        }
        if( result.koukikourei != null ){
            result.koukikourei.patientId = patientId;
        }
        if( result.roujin != null ){
            result.roujin.patientId = patientId;
        }
        if( result.kouhi1 != null ){
            result.kouhi1.patientId = patientId;
        }
        if( result.kouhi2 != null ){
            result.kouhi2.patientId = patientId;
        }
        if( result.kouhi3 != null ){
            result.kouhi3.patientId = patientId;
        }
        return this;
    }

}
