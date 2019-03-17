package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.tablebase.PracticeLogTableBase;

public class PracticeLogTable extends PracticeLogTableBase {

    public PracticeLogTable(){
        getColumnByDbColumnName("body").setPlaceHolder("?::json");
    }

}
