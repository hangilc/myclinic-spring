package jp.chang.myclinic.postgresqldev;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;

import java.util.Objects;

public class Test {

    public static void main(String[] args) throws Exception {
        if (args.length == 1 && "init".equals(args[0])) {
            prepareDb();
        } else {
            new Test().run();
        }
    }

    private PatientDTO patient1Template = new PatientDTO();

    {
        patient1Template.lastName = "田中";
        patient1Template.firstName = "一郎";
        patient1Template.lastNameYomi = "たなか";
        patient1Template.firstNameYomi = "いちろう";
        patient1Template.birthday = "1953-06-23";
        patient1Template.sex = "M";
        patient1Template.address = "杉並区荻窪123-45-67";
        patient1Template.phone = "0123-45-6789";
    }

    private ShahokokuhoDTO shahokokuho1Template = new ShahokokuhoDTO();

    {
        shahokokuho1Template.hokenshaBangou = 123455;
        shahokokuho1Template.hihokenshaKigou = "a";
        shahokokuho1Template.hihokenshaBangou = "1";
        shahokokuho1Template.patientId = 0;
        shahokokuho1Template.validFrom = "2018-06-01";
        shahokokuho1Template.validUpto = "0000-00-00";
        shahokokuho1Template.honnin = 1;
    }

    private void run() throws Exception {
        Service.setServerUrl("http://localhost:9999/json");
        try {
            PatientDTO patient = PatientDTO.copy(patient1Template);
            patient.patientId = Service.api.enterPatient(patient).join();
            {
                patient1Template.patientId = patient.patientId;
                assertEquals(patient, patient1Template, "Invalid patient 1 creation.");
                patient1Template.patientId = 0;
            }
            ShahokokuhoDTO shahokokuho = ShahokokuhoDTO.copy(shahokokuho1Template);
            shahokokuho.patientId = patient.patientId;
            shahokokuho.shahokokuhoId = Service.api.enterShahokokuho(shahokokuho).join();
            {
                shahokokuho1Template.shahokokuhoId = shahokokuho.shahokokuhoId;
                assertEquals(shahokokuho, shahokokuho, "Invalid shahokokuho 1 creation.");
                shahokokuho1Template.shahokokuhoId = 0;
            }
            int visitId = Service.api.startVisit(patient.patientId).join();
            VisitDTO visit = Service.api.getVisit(visitId).join();
            {
                TextDTO text = new TextDTO();
                text.visitId = visitId;
                text.content = "のどの痛みがある。";
                Service.api.enterText(text).join();
            }
        } finally {
            Service.stop();
        }
    }

    private void assertEquals(Object a, Object b, String message) {
        if (!Objects.equals(a, b)) {
            System.err.println(message);
            System.exit(1);
        }
    }

    private static void prepareDb() throws Exception {
        String schemaDumpPath = System.getenv("MYCLINIC_POSTGRES_SCHEMA_DUMP");
        String masterDumpPath = System.getenv("MYCLINIC_POSTGRES_MASTER_DUMP");
        if (schemaDumpPath == null) {
            System.err.println("Env MYCLINIC_POSTGRES_SCHEMA_DUMP cannot be found.");
            System.exit(1);
        }
        if (masterDumpPath == null) {
            System.err.println("Env MYCLINIC_POSTGRES_MASTER_DUMP cannot be found.");
            System.exit(1);
        }
        command("psql", "-c", "drop database myclinic_test", "-U", "postgres");
        command("psql", "-c", "create database myclinic_test owner tester", "-U", "postgres");
        command("pg_restore", "-d", "myclinic_test", "--no-owner", "-U", "tester", schemaDumpPath);
        command("pg_restore", "-d", "myclinic_test", "-U", "tester", masterDumpPath);
    }

    private static void command(String... args) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.inheritIO();
        Process process = pb.start();
        int code = process.waitFor();
        if (code != 0) {
            System.err.printf("%s failed\n", args[0]);
            System.exit(code);
        }
    }

}
