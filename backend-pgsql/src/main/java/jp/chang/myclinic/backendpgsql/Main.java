package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.mockdata.MockData;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        DB.setDataSource(PgsqlDataSource.create());
        confirmMockPatient();
        DB.proc(this::confirmMockPatient);
        DB.tx(() -> {
            Backend backend = new Backend(new PgsqlPersistence());
            MockData mockData = new MockData();
            System.out.println(backend.getPatient(1));
            PatientDTO patient = mockData.pickPatient();
            backend.enterPatient(patient);
            System.out.println(patient);
            return null;
        });
    }

    private void confirmMockPatient() {
        DB.proc(() -> {
            PatientTable patientTable = new PatientTable();
            PatientDTO patient = patientTable.getById(1);
            if (!(patient != null && patient.lastName.equals("試験") && patient.firstName.equals("データ"))) {
                throw new RuntimeException("Accessing database inappropriate for testing.");
            }
        });
    }

}

