package jp.chang.myclinic.postgresqldev;

import jp.chang.myclinic.dto.PatientDTO;

import static jp.chang.myclinic.client.Service.api;

public class Test {

    public static void main(String[] args) throws Exception {
            new Test().run();
    }

    private void run() throws Exception {
        prepareDb();
//        Service.setServerUrl("http://localhost:9999/json");
//        try {
//            PatientDTO patient = enterPatient();
//        } finally {
//            Service.stop();
//        }
    }

    private void prepareDb() throws Exception {
        String dataFile = System.getenv("MYCLINIC_POSTGRES_TEST_DATA");
        if( dataFile == null ){
            System.err.println("Env MYCLINIC_POSTGRES_TEST_DATA cannot be found.");
            System.exit(1);
        }
        command("psql", "--quiet", "-f", "postgresql-dev/sql/myclinic-postgres-schema.sql",
                "myclinic_test", "tester");
        command("psql", "--quiet", "-f", dataFile, "myclinic_test", "tester");
    }

    private void command(String... args) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.inheritIO();
        Process process = pb.start();
        int code = process.waitFor();
        if( code != 0 ){
            System.err.printf("%s failed\n", args[0]);
            System.exit(code);
        }
    }

    private PatientDTO enterPatient(){
        PatientDTO patient = new PatientDTO();
        patient.lastName = "田中";
        patient.firstName = "一郎";
        patient.lastNameYomi = "たなか";
        patient.firstNameYomi = "いちろう";
        patient.birthday = "1953-06-23";
        patient.sex = "M";
        patient.address = "杉並区荻窪123-45-67";
        patient.phone = "0123-45-6789";
        patient.patientId = api.enterPatient(patient)
                .join();
        return patient;
    }

}
