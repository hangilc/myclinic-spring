package jp.chang.myclinic.backenddb;

import org.junit.Test;
import static org.junit.Assert.*;
import jp.chang.myclinic.backenddb.SqlTranslator;
import jp.chang.myclinic.backenddb.SqlTranslator.TableInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlTranslatorTest {

    private TableInfo patientTable = new TableInfo(){

        @Override
        public String getDtoName() {
            return "Patient";
        }

        @Override
        public String getDbTableName() {
            return "patient";
        }

        @Override
        public Map<String, String> getDtoFieldToDbColumnMap() {
            Map<String, String> map = new HashMap<>();
            map.put("patientId", "patient_id");
            map.put("lastName", "last_name");
            map.put("birthday", "birthday");
            return map;
        }

        @Override
        public List<String> getColumnNames(){
            return List.of("patient_id", "last_name", "birthday");
        }
    };

    private TableInfo visitTable = new TableInfo(){

        @Override
        public String getDtoName() {
            return "Visit";
        }

        @Override
        public String getDbTableName() {
            return "visit";
        }

        @Override
        public Map<String, String> getDtoFieldToDbColumnMap() {
            Map<String, String> map = new HashMap<>();
            map.put("visitId", "visit_id");
            map.put("patientId", "patient_id");
            return map;
        }

        @Override
        public List<String> getColumnNames() {
            return List.of("visit_id", "patient_id");
        }
    };

    @Test
    public void noAliasTest(){
        SqlTranslator xlater = new SqlTranslator();
        String src = "select * from Patient";
        String dst = xlater.translate(src, patientTable);
        System.out.println(src);
        System.out.println(dst);
        assertEquals("select patient_id,last_name,birthday from patient", dst);
    }

    @Test
    public void noAliasWithFieldTest(){
        SqlTranslator xlater = new SqlTranslator();
        String src = "select * from Patient where patientId = ?";
        String dst = xlater.translate(src, patientTable);
        System.out.println(src);
        System.out.println(dst);
        assertEquals("select patient_id,last_name,birthday from patient where patient_id = ?", dst);
    }

    @Test
    public void simpleTest(){
        SqlTranslator xlater = new SqlTranslator();
        String src = "select p.* from Patient as p where p.patientId = ?";
        String dst = xlater.translate(src, patientTable, "p");
        System.out.println(src);
        System.out.println(dst);
        assertEquals("select p.patient_id,p.last_name,p.birthday from patient as p where p.patient_id = ?", dst);

    }

    @Test
    public void twoTablesTest(){
        SqlTranslator xlater = new SqlTranslator();
        String src = "select p.*, v.* from Patient as p, Visit as v where p.patientId = v.visitId and v.visitId = ?";
        String dst = xlater.translate(src, patientTable, "p", visitTable, "v");
        System.out.println(src);
        System.out.println(dst);
        assertEquals("select p.patient_id,p.last_name,p.birthday, v.visit_id,v.patient_id from patient as p, visit as v where p.patient_id = v.visit_id and v.visit_id = ?", dst);

    }


}
