package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PaymentDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

class TestCleanupWqueue {

    void run(){
        List<WqueueFullDTO> wqlist =  Context.frontend.listWqueueFull().join();
        for(WqueueFullDTO wq: wqlist){
            int visitId = wq.visit.visitId;
            MeisaiDTO meisai = Context.frontend.getMeisai(visitId).join();
            Context.frontend.endExam(visitId, meisai.charge).join();
            PaymentDTO payment = new PaymentDTO();
            payment.visitId = visitId;
            payment.amount = meisai.charge;
            payment.paytime = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
            Context.frontend.finishCashier(payment).join();
            if( Context.frontend.listDrugFull(visitId).join().size() > 0 ){
                Context.frontend.prescDone(visitId);
            }
        }
    }

}
