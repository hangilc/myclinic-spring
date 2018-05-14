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
            rcpt.seikyuuList.sort(seikyuuComparator());
            rcpt.seikyuuList.forEach(s -> {
                System.out.println("patient_id " + s.patientId);
                s.byoumeiList.forEach(b -> {
                    System.out.println(b.name);
                });
                s.visits.forEach(v -> {
                    System.out.println(v.visitedAt);
                    v.shinryouList.forEach(shinryou -> {
                        System.out.println(shinryou.shinryoucode);
                    });
                });
            });
        } catch(Exception ex){
            logger.error("Failed to run carete.", ex);
            System.exit(1);
        }
    }

    private Comparator<Seikyuu> seikyuuComparator(){
        Comparator<Seikyuu> comp = Comparator.comparing(Seikyuu::getRankTag);
        return comp.thenComparing(Comparator.comparing(s -> s.patientId));
    }

}
