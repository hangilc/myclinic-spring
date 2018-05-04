package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;

class CheckChoukiTouyakuKasan extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckChoukiTouyakuKasan.class);
    private ResolvedShinryouMap sm;

    CheckChoukiTouyakuKasan(Scope scope) {
        super(scope);
        sm = getShinryouMaster();
    }

    void check(boolean fixit) throws Exception {
        int nChoukiTouyaku = countChoukiTouyakuKasan();
        int nTokuteiKasan = countTokuteiShohouKasan();
        if( nChoukiTouyaku > 0 ){
            if( nTokuteiKasan != 0 ){
                error("特定疾患処方管理加算請求不可", fixit, () -> removeExtraTokuteiKasan(0));
            }
            if( nChoukiTouyaku > 1 ){
                error("長期投薬加算重複", fixit, this::removeExtraChoukiTouyakuKasan);
            }
        } else {
            if( nTokuteiKasan > 2 ){
                error("特定疾患処方管理加算３回以上", fixit, () -> removeExtraTokuteiKasan(2));
            }
        }
    }

    private int countChoukiTouyakuKasan(){
        return countShinryouMasterInVisits(sm.長期処方);
    }

    private int countTokuteiShohouKasan(){
        return countShinryouMasterInVisits(sm.特定疾患処方);
    }

    private void removeExtraTokuteiKasan(int nRemain) {
        removeExtraShinryouMasterInVisits(sm.特定疾患処方, nRemain);
    }

    private void removeExtraChoukiTouyakuKasan() {
        removeExtraShinryouMasterInVisits(sm.長期処方, 1);
    }

}
