package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.dto.PatientDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PatientTable extends Table<PatientDTO> {

    private List<String> columnNames = List.of(
            "patient_id",
            "last_name",
            "first_name",
            "last_name_yomi",
            "first_name_yomi",
            "birthday",
            "sex",
            "address",
            "phone"
    );

    @Override
    public String getPrimaryKey() {
        return "patient_id";
    }

    @Override
    public String getTableName() {
        return "patient";
    }

    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public PatientDTO toDTO(ResultSet rs) throws SQLException {
        PatientDTO patient = new PatientDTO();
        patient.patientId = rs.getInt(1);
        patient.lastName = rs.getString(2);
        patient.firstName = rs.getString(3);
        patient.lastNameYomi = rs.getString(4);
        patient.firstNameYomi = rs.getString(5);
        patient.birthday = rs.getObject(6, LocalDate.class).toString();
        patient.sex = rs.getString(7);
        patient.address = rs.getString(8);
        patient.phone = rs.getString(9);
        return patient;
    }

    @Override
    public void setForInsert(PreparedStatement stmt, PatientDTO patient) throws SQLException {
        stmt.setString(1, patient.lastName);
        stmt.setString(2, patient.firstName);
        stmt.setString(3, patient.lastNameYomi);
        stmt.setString(4, patient.firstNameYomi);
        stmt.setObject(5, LocalDate.parse(patient.birthday));
        stmt.setObject(6, patient.sex);
        stmt.setString(7, patient.address);
        stmt.setString(8, patient.phone);
    }

}
