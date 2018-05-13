package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Comparator;

class Create {

    private static Logger logger = LoggerFactory.getLogger(Create.class);
    private String xmlSrcFile;

    Create(String xmlSrcFile) {
        this.xmlSrcFile = xmlSrcFile;
    }

    void run(){
        try(FileInputStream ins = new FileInputStream(xmlSrcFile)){
            XmlMapper mapper = new XmlMapper();
            Rcpt rcpt = mapper.readValue(ins, Rcpt.class);
            rcpt.seikyuuList.sort(Comparator.comparing(Seikyuu::getRankTag));
            //rcpt.seikyuuList.forEach(s -> System.out.println("patient_id " + s.patientId));
            rcpt.seikyuuList.stream().filter(s -> s.patientId == 2213 || s.patientId == 430 )
                    .forEach(s -> System.out.println(s.patientId + " " + s.getRankTag()));
        } catch(Exception ex){
            logger.error("Failed to run carete.", ex);
            System.exit(1);
        }
    }

}
