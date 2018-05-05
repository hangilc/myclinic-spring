package jp.chang.myclinic.rcpt.check;

class CheckDiseaseExists extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckDiseaseExists.class);

    CheckDiseaseExists(Scope scope) {
        super(scope);
    }

    void check(){
        if( getDiseases().size() == 0 ){
            error("病名なし");
        }
    }

}
