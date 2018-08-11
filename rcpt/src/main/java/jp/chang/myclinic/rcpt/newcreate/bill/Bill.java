package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.newcreate.input.Rcpt;
import jp.chang.myclinic.rcpt.newcreate.input.Seikyuu;
import jp.chang.myclinic.rcpt.newcreate.output.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Bill {

    private static Logger logger = LoggerFactory.getLogger(Bill.class);
    private Rcpt rcpt;
    private Output out;
    private ResolvedShinryouMap resolvedShinryouMap;
    private Map<Integer, String> shinryouAliasMap;
    private HoukatsuKensaRevision.Revision houkatsuKensaRevision;

    public Bill(Rcpt rcpt, Output output, ResolvedShinryouMap resolvedShinryouMap,
                HoukatsuKensaRevision.Revision houkatsuKensaRevision) {
        this.rcpt = rcpt;
        this.out = output;
        this.resolvedShinryouMap = resolvedShinryouMap;
        this.shinryouAliasMap = ShinryouAliasMap.create(resolvedShinryouMap);
        this.houkatsuKensaRevision = houkatsuKensaRevision;
    }

    public void run() {
        for (Seikyuu seikyuu : rcpt.seikyuuList) {
            out.print("rcpt_begin");
            runProlog();
            PatientBill patientBill = new PatientBill(seikyuu, out, resolvedShinryouMap, shinryouAliasMap,
                    houkatsuKensaRevision);
            patientBill.run();
            out.print("rcpt_end");
        }
    }

    private void runProlog() {
        out.printStr("kikancode", rcpt.kikancode);
        out.printInt("fukenbangou", rcpt.todoufukenBangou);
        out.printInt("shinryou.nen", rcpt.nen);
        out.printInt("shinryou.tsuki", rcpt.month);
        out.printStr("shozaichimeishou.line1", rcpt.clinicAddress);
        out.printStr("shozaichimeishou.line2", rcpt.clinicPhone);
        out.printStr("shozaichimeishou.line4", rcpt.clinicName);
    }



}
