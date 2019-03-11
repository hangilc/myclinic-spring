package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    private void run(String[] args) throws Exception {
        confirmMockPatient();
//        PatientTable patientTable = new PatientTable();
//        PatientDTO p = new PatientDTO();
//        p.lastName = "田中";
//        p.firstName = "光一";
//        p.lastNameYomi = "たなか";
//        p.firstNameYomi = "こういち";
//        p.birthday = "1987-02-12";
//        p.sex = "M";
//        p.address = "addr";
//        p.phone = "03-1234-5678";
//        DB.get(conn -> {
//            patientTable.delete(conn, 7099);
//            System.out.println(patientTable.getById(conn, 7099));
//            return null;
//        });
    }

    private void confirmMockPatient(){
        DB.get(conn -> {
            PatientTable patientTable = new PatientTable(conn);
            PatientDTO patient = patientTable.getById(1);
            if( !(patient.lastName.equals("試験") && patient.firstName.equals("データ")) ){
                throw new RuntimeException("Accessing database inappropriate for testing.");
            }
            return null;
        });
    }

}

