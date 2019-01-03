package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.table.*;

class PostgreSqlDb extends MyclinicDb {

    PostgreSqlDb(){
        this.iyakuhinMaster = new IyakuhinMaster();
        this.shinryouMaster = new ShinryouMaster();
        this.kizaiMaster = new KizaiMaster();
        this.byoumeiMaster = new ByoumeiMaster();
        this.patient = new Patient();
    }

}
