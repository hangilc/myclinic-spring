package jp.chang.myclinic.dbxfer.table;

import jp.chang.myclinic.dbxfer.db.DateColumn;
import jp.chang.myclinic.dbxfer.db.SerialColumn;
import jp.chang.myclinic.dbxfer.db.StringColumn;
import jp.chang.myclinic.dbxfer.db.Table;

import static jp.chang.myclinic.dbxfer.table.PatientEnum.*;

public class Patient extends Table<PatientEnum> {

    public Patient() {
        super("patient");
        setColumn(PATIENT_ID, new SerialColumn("patient_id"));
        setColumn(LAST_NAME, new StringColumn("last_name"));
        setColumn(FIRST_NAME, new StringColumn("first_name"));
        setColumn(LAST_NAME_YOMI, new StringColumn("last_name_yomi"));
        setColumn(FIRST_NAME_YOMI, new StringColumn("first_name_yomi"));
        setColumn(SEX, new StringColumn("sex"));
        setColumn(BIRTHDAY, new DateColumn("birthday"));
        setColumn(ADDRESS, new StringColumn("address"));
        setColumn(PHONE, new StringColumn("phone"));
    }

    @Override
    public PatientEnum[] listColumnEnums() {
        return PatientEnum.values();
    }
}