package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    interface ConsumerSQL<T> {
        default void accept(T t){

        }
    }

    private void run(String[] args) throws Exception {
        PatientTable patientTable = new PatientTable();
        String sql = "select " + patientTable.cols() + " from patient where patient_id = ?";
        PatientDTO patient = DB.get(sql, stmt -> stmt.setInt(1, 1), patientTable::toDTO);
        System.out.println(patient);
    }

}

