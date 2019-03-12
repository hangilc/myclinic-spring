package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.dto.PatientDTO;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class PatientTable extends Table<PatientDTO> {

    private static final List<Column<PatientDTO>> columns;

    static {
        columns = List.of(
                new Column<PatientDTO>("patient_id", true, true,
                        (rs, dto) -> dto.patientId = rs.getInt("patient_id"),
                        dto -> dto.patientId
                ),
                new Column<PatientDTO>("last_name",
                        (rs, dto) -> dto.lastName = rs.getString("last_name"),
                        dto -> dto.lastName
                ),
                new Column<PatientDTO>("first_name",
                        (rs, dto) -> dto.firstName = rs.getString("first_name"),
                        dto -> dto.firstName
                ),
                new Column<PatientDTO>("last_name_yomi",
                        (rs, dto) -> dto.lastNameYomi = rs.getString("last_name_yomi"),
                        dto -> dto.lastNameYomi
                ),
                new Column<PatientDTO>("first_name_yomi",
                        (rs, dto) -> dto.firstNameYomi = rs.getString("first_name_yomi"),
                        dto -> dto.firstNameYomi
                ),
                new Column<PatientDTO>("birthday",
                        (rs, dto) -> dto.birthday = rs.getObject("birthday", LocalDate.class).toString(),
                        dto -> LocalDate.parse(dto.birthday)
                ),
                new Column<PatientDTO>("sex",
                        (rs, dto) -> dto.sex = rs.getString("sex"),
                        dto -> dto.sex
                ),
                new Column<PatientDTO>("address",
                        (rs, dto) -> dto.address = rs.getString("address"),
                        dto -> dto.address
                ),
                new Column<PatientDTO>("phone",
                        (rs, dto) -> dto.phone = rs.getString("phone"),
                        dto -> dto.phone
                )
        );
    }

    public PatientTable(Connection conn) {
        super(conn);
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
