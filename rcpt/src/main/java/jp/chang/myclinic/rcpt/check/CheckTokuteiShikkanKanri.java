package jp.chang.myclinic.rcpt.check;

class CheckTokuteiShikkanKanri extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckTokuteiShikkanKanri.class);

    CheckTokuteiShikkanKanri(Scope scope) {
        super(scope);
    }

    void check(boolean fixit){
        int n = countKanri();
        if( n > 2 ){
            error("特定疾患療養管理料が３回以上", fixit, () ->
                removeExtraShinryouMasterInVisits(getShinryouMaster().特定疾患管理, 2)
            );
        }
    }

    private int countKanri(){
        return countShinryouMasterInVisits(getShinryouMaster().特定疾患管理);
    }

}
