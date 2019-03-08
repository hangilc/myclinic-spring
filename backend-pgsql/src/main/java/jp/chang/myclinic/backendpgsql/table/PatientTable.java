package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.dto.PatientDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class PatientTable {

    private List<String> cols = List.of(
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

    private String colsForInsert = String.join(",", cols.subList(1, cols.size()));
    private String paraForInsert = String.join(",", cols.subList(1, cols.size()).stream()
            .map(c -> "?").collect(toList()));
    private String colsForQuery = String.join(",", cols);

    public String cols() {
        return cols("");
    }

    public String cols(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return colsForQuery;
        } else {
            return String.join(",", cols.stream().map(c -> prefix + c).collect(toList()));
        }
    }

    public int insert(Connection conn, PatientDTO patient) throws SQLException {
        String sql = "insert into patient (" + colsForInsert + ") " +
                "values (" + paraForInsert + ") returning patient_id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, patient.lastName);
        stmt.setString(2, patient.firstName);
        stmt.setString(3, patient.lastNameYomi);
        stmt.setString(4, patient.firstNameYomi);
        stmt.setObject(5, LocalDate.parse(patient.birthday));
        stmt.setObject(6, patient.sex);
        stmt.setString(7, patient.address);
        stmt.setString(8, patient.phone);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

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

}
