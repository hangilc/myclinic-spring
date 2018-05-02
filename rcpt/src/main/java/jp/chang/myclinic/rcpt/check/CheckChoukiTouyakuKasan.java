package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;

import java.util.List;

class CheckChoukiTouyakuKasan extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckChoukiTouyakuKasan.class);
    private List<VisitFull2DTO> visits;
    private Masters masters;

    CheckChoukiTouyakuKasan(List<VisitFull2DTO> visits, Masters masters) {
        this.visits = visits;
        this.masters = masters;
    }

    void check(boolean fixit) throws Exception {
        int nChoukiTouyaku = countChoukiTouyakuKasan();
        int nTokuteiKasan = countTokuteiShohouKasan();
        if( nChoukiTouyaku > 0 ){
            if( nTokuteiKasan != 0 ){
                error("特定疾患処方管理加算請求不可");
                if( fixit ){
                    removeExtraTokuteiKasan(0);
                    info("FIXED");
                }
            }
            if( nChoukiTouyaku > 1 ){
                error("長期投薬加算重複");
                if( fixit ){
                    removeExtraChoukiTouyakuKasan();
                    info("FIXED");
                }
            }
        } else {
            if( nTokuteiKasan > 2 ){
                error("特定疾患処方管理加算３回以上");
                if( fixit ){
                    removeExtraTokuteiKasan(2);
                    info("FIXED");
                }
            }
        }
    }

    private int countChoukiTouyakuKasan(){
        return Helper.countShinryoucodeInVisits(visits, masters.長期投薬加算.shinryoucode);
    }

    private int countTokuteiShohouKasan(){
        return Helper.countShinryoucodeInVisits(visits, masters.特定疾患処方管理加算.shinryoucode);
    }

    private void removeExtraTokuteiKasan(int nRemain) throws Exception {
        Helper.removeExtraShinryouFromVisits(visits, masters.特定疾患処方管理加算, nRemain);
    }

    private void removeExtraChoukiTouyakuKasan() throws Exception {
        Helper.removeExtraShinryouFromVisits(visits, masters.長期投薬加算, 1);
    }

}
