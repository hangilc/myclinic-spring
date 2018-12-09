package jp.chang.myclinic.dbxfer.mysql;

import jp.chang.myclinic.dbxfer.db.DateColumn;

import static jp.chang.myclinic.dbxfer.table.PatientEnum.*;

public class Patient extends jp.chang.myclinic.dbxfer.table.Patient {

    public Patient() {
        setColumn(BIRTHDAY, new DateColumn("birth_day"));
    }

}