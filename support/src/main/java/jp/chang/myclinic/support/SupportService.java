package jp.chang.myclinic.support;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class SupportService {

    private SupportSet ss;

    public SupportService(SupportSet supportSet) {
        this.ss = supportSet;
    }

//    public ShinryouDTO enterShinryouByName(int visitId, String name) {
//        int shinryoucode = ss.shinryoucodeResolver.resolveShinryoucodeByKey(name);
//        if (shinryoucode == 0) {
//            throw new RuntimeException("Cannot find shinryou: " + name);
//        }
//        VisitDTO visit = getVisit(visitId);
//        LocalDate at = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
//        ShinryouMasterDTO master = getShinryouMaster(shinryoucode, at);
//        if (master == null) {
//            throw new RuntimeException(String.format("Shinryou (%s) is not available at %s", name, at.toString()));
//        }
//        ShinryouDTO shinryou = new ShinryouDTO();
//        shinryou.visitId = visitId;
//        shinryou.shinryoucode = shinryoucode;
//        enterShinryou(shinryou);
//        return shinryou;
//    }


}
