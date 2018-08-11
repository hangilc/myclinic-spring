package jp.chang.myclinic.rcpt.newcreate.bill;

import jp.chang.myclinic.rcpt.newcreate.output.Output;

import java.util.LinkedHashSet;
import java.util.Set;

public class Shuukei {

    //private static Logger logger = LoggerFactory.getLogger(Shuukei.class);
    private String prefix;
    private Set<Integer> tankaSet = new LinkedHashSet<>();
    private int count;
    private int ten;
    private boolean printTanka = true;
    private boolean printCount = true;

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

    public boolean getPrintCount() {
        return printCount;
    }

    public void setPrintCount(boolean printCount) {
        this.printCount = printCount;
    }

    public void print(Output output){
        if( ten == 0 ){
            return;
        }
        Integer tankaValue = null;
        if( printTanka && tankaSet.size() == 1 ){
            tankaValue = tankaSet.toArray(new Integer[]{})[0];
        }
        Integer countValue = null;
        if( printCount ){
            countValue = count;
        }
        output.printShuukei(prefix, tankaValue, countValue, ten);
    }

}
