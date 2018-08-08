package jp.chang.myclinic.rcpt.newcreate;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jp.chang.myclinic.rcpt.newcreate.input.Rcpt;
import jp.chang.myclinic.rcpt.newcreate.input.Seikyuu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Comparator;

public class Create {

    private static Logger logger = LoggerFactory.getLogger(Create.class);

    public static void run(String xmlDataFile){
        try (FileInputStream ins = new FileInputStream(xmlDataFile)) {
            XmlMapper mapper = new XmlMapper();
            Rcpt rcpt = mapper.readValue(ins, Rcpt.class);
            rcpt.seikyuuList.sort(seikyuuComparator());
//            outputRcpt(rcpt);
            System.out.println("dummy");
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
