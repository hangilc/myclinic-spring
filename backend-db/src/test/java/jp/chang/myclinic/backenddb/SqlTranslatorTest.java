package jp.chang.myclinic.backenddb;

import org.junit.Test;
import static org.junit.Assert.*;
import jp.chang.myclinic.backenddb.SqlTranslator;
import jp.chang.myclinic.backenddb.SqlTranslator.TableInfo;

import java.util.Collections;
import java.util.HashMap;
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
            return map;
        }
    };

    @Test
    public void noAliasTest(){
        SqlTranslator xlater = new SqlTranslator();
        String src = "select * from Patient";
        String dst = xlater.translate(src, patientTable);
        System.out.println(src);
        System.out.println(dst);
        assertEquals("select * from patient", dst);
    }

    @Test
    public void noAliasWithFieldTest(){
        SqlTranslator xlater = new SqlTranslator();
        String src = "select * from Patient where patientId = ?";
        String dst = xlater.translate(src, patientTable);
        System.out.println(src);
        System.out.println(dst);
        assertEquals("select * from patient where patient_id = ?", dst);
    }

    @Test
    public void simpleTest(){
        SqlTranslator xlater = new SqlTranslator();
        String src = "select p.* from Patient as p where p.patientId = ?";
        String dst = xlater.translate(src, patientTable, "p");
        System.out.println(src);
        System.out.println(dst);
        assertEquals("select p.* from patient as p where p.patient_id = ?", dst);

    }


}
