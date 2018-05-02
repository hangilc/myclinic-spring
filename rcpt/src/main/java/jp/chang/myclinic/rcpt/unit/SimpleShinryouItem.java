package jp.chang.myclinic.rcpt.unit;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SimpleShinryouItem implements Countable {

    private static Logger logger = LoggerFactory.getLogger(SimpleShinryouItem.class);
    private ShinryouMasterDTO master;
    private int count;

    SimpleShinryouItem(ShinryouMasterDTO master) {
        this.master = master;
        count = 1;
    }

    @Override
    public void incCount(int n){
        count += n;
    }

    @Override
    public int getCount(){
        return count;
    }

    int getShinryoucode(){
        return master.shinryoucode;
    }

    @Override
    public String toString() {
        return "SimpleShinryouItem{" +
                "master=" + master +
                ", count=" + count +
                '}';
    }
}
