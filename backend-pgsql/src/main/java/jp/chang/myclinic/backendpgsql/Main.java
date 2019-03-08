package jp.chang.myclinic.backendpgsql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    interface ConsumerSQL<T> {
        default void accept(T t){

        }
    }

    private void run(String[] args) throws Exception {
        HikariConfig config = new HikariConfig();
        String host = System.getenv("MYCLINIC_POSTGRES_HOST");
        String user = System.getenv("MYCLINIC_POSTGRES_USER");
        String pass = System.getenv("MYCLINIC_POSTGRES_PASS");
        config.setJdbcUrl("jdbc:postgresql://" + host + "/myclinic");
        config.setUsername(user);
        config.setPassword(pass);
        HikariDataSource ds = new HikariDataSource(config);
        try (Connection conn = ds.getConnection()) {
            PatientTable patientTable = new PatientTable();
//            PatientDTO patient = new PatientDTO();
//            patient.lastName = "試験";
//            patient.firstName = "データ";
//            patient.lastNameYomi = "しけん";
//            patient.firstNameYomi = "でーた";
//            patient.birthday = "1957-02-08";
//            patient.sex = "F";
//            patient.address = "ADDRESS";
//            patient.phone = "PHONE";
//            patient.patientId = patientTable.insert(conn, patient);
            Transaction tx = new Transaction(conn);
            String sql = "select " + patientTable.cols() + " from patient where patient_id = ?";
            System.out.println(sql);
            int patientId = 1;
            PatientDTO patient = tx.selectOne(
                    sql,
                    stmt -> {
                        stmt.setInt(1, patientId);
                    },
                    patientTable::toDTO
            );
            System.out.println(patient);
        }
    }

}

