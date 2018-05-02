package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;

import java.util.List;
import java.util.stream.Collectors;

class CheckTokuteiShikkanKanri extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckTokuteiShikkanKanri.class);
    private List<VisitFull2DTO> visits;
    private Masters masters;

    CheckTokuteiShikkanKanri(List<VisitFull2DTO> visits, Masters masters) throws Exception {
        this.visits = visits;
        this.masters = masters;
    }

    void check(boolean fixit) throws Exception {
        int n = countKanri();
        if( n > 2 ){
            error("特定疾患療養管理料が３回以上");
            if( fixit ){
                removeKanri(n - 2);
                info("FIXED");
            }
        }
    }

    private int countKanri(){
        return Helper.countShinryoucodeInVisits(visits, masters.特定疾患管理料.shinryoucode);
    }

    private void removeKanri(int n) throws Exception {
        int kanricode = masters.特定疾患管理料.shinryoucode;
        List<ShinryouFullDTO> kanriList = Helper.filterShinryouInVisits(visits,
                s -> s.master.shinryoucode == kanricode);
        assert kanriList.size() == n + 2;
        List<Integer> shinryouIds = kanriList.subList(2, n + 2).stream()
                .map(s -> s.shinryou.shinryouId).collect(Collectors.toList());
        Service.api.batchDeleteShinryou(shinryouIds);
    }

}
