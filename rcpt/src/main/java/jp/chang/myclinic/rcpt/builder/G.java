package jp.chang.myclinic.rcpt.builder;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

class G {

    //private static Logger logger = LoggerFactory.getLogger(G.class);
    private static AtomicInteger seedId = new AtomicInteger(0);
    private static String todayValue = LocalDate.now().toString();

    private G() { }

    static int genid(){
        return seedId.incrementAndGet();
    }

    static String gensym(){
        int id = genid();
        return "GENERATED-" + id;
    }

    static String today(){
        return todayValue;
    }

    static String nullDate(){
        return "0000-00-00";
    }
}
