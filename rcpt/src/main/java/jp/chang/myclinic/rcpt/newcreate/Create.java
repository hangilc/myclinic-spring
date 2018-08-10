package jp.chang.myclinic.rcpt.newcreate;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.Common;
import jp.chang.myclinic.rcpt.newcreate.bill.Bill;
import jp.chang.myclinic.rcpt.newcreate.input.Rcpt;
import jp.chang.myclinic.rcpt.newcreate.input.Seikyuu;
import jp.chang.myclinic.rcpt.newcreate.output.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Comparator;

public class Create {

    private static Logger logger = LoggerFactory.getLogger(Create.class);

    public static void run(String xmlDataFile, PrintStream printStream){
        try (FileInputStream ins = new FileInputStream(xmlDataFile)) {
            XmlMapper mapper = new XmlMapper();
            Rcpt rcpt = mapper.readValue(ins, Rcpt.class);
            rcpt.seikyuuList.sort(seikyuuComparator());
            Common.MasterMaps masterMaps = Common.getMasterMaps(rcpt.getDate(1));
            ResolvedShinryouMap shinryouMasterMap = masterMaps.resolvedMap.shinryouMap;
            Output output = new Output(printStream);
            Bill bill = new Bill(rcpt, output, shinryouMasterMap);
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
