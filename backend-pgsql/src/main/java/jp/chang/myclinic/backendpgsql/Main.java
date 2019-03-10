package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

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
        PatientDTO p = new PatientDTO();
        p.lastName = "田中";
        p.firstName = "光一";
        p.lastNameYomi = "たなか";
        p.firstNameYomi = "こういち";
        p.birthday = "1987-02-12";
        p.sex = "M";
        p.address = "addr";
        p.phone = "03-1234-5678";
        DB.get(conn -> {
            patientTable.delete(conn, 7099);
            System.out.println(patientTable.getById(conn, 7099));
            return null;
        });
    }

}

