package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Naifuku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RcptNaifukuItem {

    private static Logger logger = LoggerFactory.getLogger(RcptNaifukuItem.class);

    RcptNaifukuItem(Naifuku drug) {

    }

    boolean canExtend(Naifuku drug){
        return false;
    }

    void extend(Naifuku drug){

    }

}
