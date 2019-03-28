package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.logdto.practicelog.VisitCreated;
import jp.chang.myclinic.logdto.practicelog.WqueueCreated;

import java.time.LocalDateTime;
import java.util.List;

public class VisitTester extends TesterBase {

    public VisitTester(Backend backend) {
        super(backend);
    }

    @DbTest
    public void testStartVisit(){
        System.out.println("visit:startVisit");
        int serialId = backend.getLastPracticeLogId();
        VisitDTO visit = backend.startVisit(198, LocalDateTime.now());
        List<PracticeLogDTO> logs = backend.listPracticeLogSince(serialId);
        confirm(logs.size() == 2);
        PracticeLogDTO vlog = logs.get(0);
        confirm(vlog.isVisitCreated());
        PracticeLogDTO wlog = logs.get(1);
        confirm(wlog.isWqueueCreated());
        WqueueCreated wcreated = wlog.asWqueueCreated();
        confirm(wcreated.created.visitId == visit.visitId);
        confirm(wcreated.created.waitState == WqueueWaitState.WaitExam.getCode());
    }
}
