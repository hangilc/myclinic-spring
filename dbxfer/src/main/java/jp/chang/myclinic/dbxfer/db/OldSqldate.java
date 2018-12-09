package jp.chang.myclinic.dbxfer.db;

import java.time.LocalDate;

public class OldSqldate {

    private String rep;

    public OldSqldate(String rep) {
        this.rep = rep;
    }

    public OldSqldate(LocalDate date){
        if( date == null ){
            this.rep = "0000-00-00";
        } else {
            this.rep = date.toString();
        }
    }

    public String getRep(){
        return rep;
    }

}
