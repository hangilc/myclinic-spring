package jp.chang.myclinic.dbxfer.db;

import java.time.LocalDate;

public class Sqldate {

    private String rep;

    public Sqldate(String rep) {
        this.rep = rep;
    }

    public Sqldate(LocalDate date){
        this.rep = date.toString();
    }

    public String getRep(){
        return rep;
    }

}
