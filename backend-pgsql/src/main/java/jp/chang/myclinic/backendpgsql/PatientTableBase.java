package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.dto.PatientDTO;

import java.time.LocalDate;
import java.util.List;

public class PatientTableBase extends Table<PatientDTO> {

    private static List<Column<PatientDTO>> columns;

    static {
        columns = List.of(
                new Column<PatientDTO>(
                        "patient_id",
                        true,
                        true,
                        (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                        (rs, i, dto) -> dto.patientId = rs.getObject(i, Integer.class)
                ),
                new Column<PatientDTO>(
                        "last_name",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.lastName),
                        (rs, i, dto) -> dto.lastName = rs.getObject(i, String.class)
                ),
                new Column<PatientDTO>(
                        "first_name",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.firstName),
                        (rs, i, dto) -> dto.firstName = rs.getObject(i, String.class)
                ),
                new Column<PatientDTO>(
                        "last_name_yomi",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.lastNameYomi),
                        (rs, i, dto) -> dto.lastNameYomi = rs.getObject(i, String.class)
                ),
                new Column<PatientDTO>(
                        "first_name_yomi",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.firstNameYomi),
                        (rs, i, dto) -> dto.firstNameYomi = rs.getObject(i, String.class)
                ),
                new Column<PatientDTO>(
                        "sex",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.sex),
                        (rs, i, dto) -> dto.sex = rs.getObject(i, String.class)
                ),
                new Column<PatientDTO>(
                        "birthday",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.birthday)),
                        (rs, i, dto) -> dto.birthday = rs.getObject(i, LocalDate.class).toString()
                ),
                new Column<PatientDTO>(
                        "address",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.address),
                        (rs, i, dto) -> dto.address = rs.getObject(i, String.class)
                ),
                new Column<PatientDTO>(
                        "phone",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.phone),
                        (rs, i, dto) -> dto.phone = rs.getObject(i, String.class)
                )
        );
    }

    @Override
    protected String getTableName() {
        return "patient";
    }

    @Override
    protected Class<PatientDTO> getClassDTO() {
        return PatientDTO.class;
    }

    @Override
    protected List<Column<PatientDTO>> getColumns() {
        return columns;
    }

}
