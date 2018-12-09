package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.mysql.*;

class MySqlDb extends MyclinicDb {

    MySqlDb() {
        this.iyakuhinMaster = new IyakuhinMaster();
        this.shinryouMaster = new ShinryouMaster();
        this.kizaiMaster = new KizaiMaster();
        this.byoumeiMaster = new ByoumeiMaster();
    }

}
