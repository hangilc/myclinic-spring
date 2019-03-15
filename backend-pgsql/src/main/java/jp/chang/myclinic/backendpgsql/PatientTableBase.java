package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
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
                        (rs, dto) -> dto.patientId = rs.getObject("patient_id", Integer.class)
                ),
                new Column<PatientDTO>(
                        "last_name",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.lastName),
                        (rs, dto) -> dto.lastName = rs.getObject("last_name", String.class)
                ),
                new Column<PatientDTO>(
                        "first_name",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.firstName),
                        (rs, dto) -> dto.firstName = rs.getObject("first_name", String.class)
                ),
                new Column<PatientDTO>(
                        "last_name_yomi",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.lastNameYomi),
                        (rs, dto) -> dto.lastNameYomi = rs.getObject("last_name_yomi", String.class)
                ),
                new Column<PatientDTO>(
                        "first_name_yomi",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.firstNameYomi),
                        (rs, dto) -> dto.firstNameYomi = rs.getObject("first_name_yomi", String.class)
                ),
                new Column<PatientDTO>(
                        "sex",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.sex),
                        (rs, dto) -> dto.sex = rs.getObject("sex", String.class)
                ),
                new Column<PatientDTO>(
                        "birthday",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.birthday)),
                        (rs, dto) -> dto.birthday = rs.getObject("birthday", LocalDate.class).toString()
                ),
                new Column<PatientDTO>(
                        "address",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.address),
                        (rs, dto) -> dto.address = rs.getObject("address", String.class)
                ),
                new Column<PatientDTO>(
                        "phone",
                        false,
                        false,
                        (stmt, i, dto) -> stmt.setString(i, dto.phone),
                        (rs, dto) -> dto.phone = rs.getObject("phone", String.class)
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

    public String patientId(){
        return "patient_id";
    }

    public String patientId(String prefix){
        return prefix + "." + "patient_id";
    }

    public String lastName(){
        return "last_name";
    }

    public String lastName(String prefix){
        return prefix + "." + "last_name";
    }
}
