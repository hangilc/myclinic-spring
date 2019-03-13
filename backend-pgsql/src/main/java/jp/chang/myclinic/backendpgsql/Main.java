package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

public class Main {

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        confirmMockPatient();
        DB.get(conn -> {
            Table.setConnection(conn);
            PersistencePgsql persistence = new PersistencePgsql();
            PatientDTO p = persistence.getPatient(198);
            System.out.println(p);
            return null;
        });
    }

    private void confirmMockPatient(){
        DB.get(conn -> {
            Table.setConnection(conn);
            PatientTable patientTable = new PatientTable();
            PatientDTO patient = patientTable.getById(1);
            if( !(patient != null && patient.lastName.equals("試験") && patient.firstName.equals("データ")) ){
                throw new RuntimeException("Accessing database inappropriate for testing.");
            }
            return null;
        });
    }

}

