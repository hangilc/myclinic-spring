package jp.chang.myclinic;

import jp.chang.myclinic.drawer.receipt.ReceiptDrawerData;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.HokenUtil;

import java.time.LocalDate;

/**
 * Created by hangil on 2017/05/21.
 */
public class ReceiptDrawerDataCreator {

    public static ReceiptDrawerData create(PatientDTO patient, VisitDTO visit, MeisaiDTO meisai){
        ReceiptDrawerData data = new ReceiptDrawerData();
        data.setPatientName(patient.lastName + patient.firstName);
        data.setChargeByInt(meisai.charge);
        data.setVisitDate(DateTimeUtil.formatSqlDateTime(visit.visitedAt, DateTimeUtil.kanjiFormatter1));
        data.setIssueDate(DateTimeUtil.toKanji(LocalDate.now(), DateTimeUtil.kanjiFormatter1));
        data.setPatientId("" + patient.patientId);
        data.setHoken(hokenRep(meisai.hoken));
        data.setFutanWari("" + meisai.futanWari);
        return data;
    }

    private static String hokenRep(HokenDTO hoken){
        if( hoken != null ){
            String rep = HokenUtil.hokenRep(hoken);
            if( !rep.isEmpty() ){
                return rep;
            }
        }
        return "";
    }
}
