package jp.chang.myclinic.rcpt.newcreate.output;

import java.util.LinkedHashSet;
import java.util.Set;

public class Shuukei {

    //private static Logger logger = LoggerFactory.getLogger(Shuukei.class);
    private String prefix;
    private Set<Integer> tankaSet = new LinkedHashSet<>();
    private int count;
    private int ten;
    private boolean printTanka = true;

    public Shuukei(String prefix) {
        this.prefix = prefix;
    }

    public void add(int tanka){
        tankaSet.add(tanka);
        count += 1;
        ten += tanka;
    }

    public boolean getPrintTanka() {
        return printTanka;
    }

    public void setPrintTanka(boolean printTanka) {
        this.printTanka = printTanka;
    }

    public void print(Output output){
        if( ten == 0 ){
            return;
        }
        Integer tankaValue = null;
        if( printTanka && tankaSet.size() == 1 ){
            tankaValue = tankaSet.toArray(new Integer[]{})[0];
        }
        output.printShuukei(prefix, tankaValue, count, ten);
    }

}
