package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jp.chang.myclinic.rcpt.Common;
import jp.chang.myclinic.rcpt.create.bill.Bill;
import jp.chang.myclinic.rcpt.create.bill.HoukatsuKensaRevision;
import jp.chang.myclinic.rcpt.create.input.Rcpt;
import jp.chang.myclinic.rcpt.create.input.Seikyuu;
import jp.chang.myclinic.rcpt.create.output.Output;
import jp.chang.myclinic.rcpt.resolvedmap.ResolvedMap;
import jp.chang.myclinic.rcpt.resolvedmap.ResolvedShinryouMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Comparator;

public class Create {

    private static Logger logger = LoggerFactory.getLogger(Create.class);

    public static void run(String xmlDataFile, PrintStream printStream){
        try (FileInputStream ins = new FileInputStream(xmlDataFile)) {
            XmlMapper mapper = new XmlMapper();
            Rcpt rcpt = mapper.readValue(ins, Rcpt.class);
            rcpt.seikyuuList.sort(seikyuuComparator());
            LocalDate at = rcpt.getDate(1);
            ResolvedMap resolvedMap = Common.getMasterMaps(at);
            ResolvedShinryouMap shinryouMasterMap = resolvedMap.shinryouMap;
            HoukatsuKensaRevision houkatsuKensaRevision = HoukatsuKensaRevision.load();
            HoukatsuKensaRevision.Revision revision = houkatsuKensaRevision.findRevision(at);
            Output output = new Output(printStream);
            Bill bill = new Bill(rcpt, output, shinryouMasterMap, revision);
            bill.run();
        } catch (Exception ex) {
            logger.error("Failed to run create.", ex);
            System.exit(1);
        }
    }

    private static Comparator<Seikyuu> seikyuuComparator() {
        Comparator<Seikyuu> comp = Comparator.comparing(Seikyuu::getRankTag);
        return comp.thenComparing(s -> s.patientId);
    }


}
