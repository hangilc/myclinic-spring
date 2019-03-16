package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        confirmMockPatient();
        DB.tx(conn -> {
            Table.setConnection(conn);
            PatientTable patientTable = new PatientTable();
            System.out.println(patientTable.searchPatient("田中", "うた"));
            System.out.println(patientTable.listRecentlyRegisteredPatient(3));
//            PatientDTO patient = new PatientDTO();
//            patient.lastName = "田中";
//            patient.firstName = "一郎太";
//            patient.lastNameYomi = "たなか";
//            patient.firstNameYomi = "いちろうた";
//            patient.sex = "M";
//            patient.birthday = "1950-04-12";
//            patient.address = "address";
//            patient.phone = "phone";
//            PatientTableBase patientTable = new PatientTableBase();
//            patientTable.insert(patient);
//            System.out.println(patient);
            return null;
        });
    }

    private void confirmMockPatient(){
        DB.get(conn -> {
            Table.setConnection(conn);
            PatientTableBase patientTable = new PatientTableBase();
            PatientDTO patient = patientTable.getById(1);
            if( !(patient != null && patient.lastName.equals("試験") && patient.firstName.equals("データ")) ){
                throw new RuntimeException("Accessing database inappropriate for testing.");
            }
            return null;
        });
    }

}

