package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;

import java.util.List;

class CheckChouki extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckChouki.class);
    private List<VisitFull2DTO> visits;
    private Masters masters;

    CheckChouki(List<VisitFull2DTO> visits, Masters masters) {
        this.visits = visits;
        this.masters = masters;
    }

    void check(boolean fixit) throws Exception {
        int choukiCount = Helper.countShinryoucodeInVisits(visits, masters.調基.shinryoucode);
        if (shohousenExists()) {
            if (choukiCount > 0) {
                error("処方せん料、調基の同時算定");
            }
        } else {
            if (!Helper.drugExistsInVisits(visits)) {
                if (choukiCount > 0) {
                    error("調基請求不可");
                }
            } else {
                if (choukiCount > 1) {
                    error("調基重複");
                    if (fixit) {
                        fixChoukiChoufuku();
                        info("FIXED");
                    }
                } else if (choukiCount == 0) {
                    error("調基抜け");
                    if (fixit) {
                        fixChoukiNuke();
                        info("FIXED");
                    }
                }
            }
        }
    }

    private boolean shohousenExists(){
        return Helper.shinryoucodeExistsInVisits(visits, masters.処方せん料.shinryoucode) ||
                Helper.shinryoucodeExistsInVisits(visits, masters.処方せん料７.shinryoucode);
    }

    private void fixChoukiChoufuku() throws Exception {
        List<ShinryouFullDTO> choukiList = Helper.filterShinryouInVisits(visits,
                s -> s.master.shinryoucode == masters.調基.shinryoucode);
        assert choukiList.size() > 1;
        for (int i = 1; i < choukiList.size(); i++) {
            ShinryouDTO shinryou = choukiList.get(i).shinryou;
            boolean ok = Service.api.deleteShinryouCall(shinryou.shinryouId).execute().body();
            assert ok;
        }
    }

    private void fixChoukiNuke() throws Exception {
        List<VisitFull2DTO> targets = Helper.filterVisitInVisits(visits, v -> v.drugs.size() > 0);
        assert targets.size() > 0;
        int targetVisitId = targets.get(0).visit.visitId;
        ShinryouDTO shinryou = new ShinryouDTO();
        shinryou.visitId = targetVisitId;
        shinryou.shinryoucode = masters.調基.shinryoucode;
        int shinryouId = Service.api.enterShinryouCall(shinryou).execute().body();
        assert shinryouId > 0;
    }

}
