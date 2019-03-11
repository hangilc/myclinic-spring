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
                        (o, dto) -> { dto.patientId = (Integer) o;},
                        dto -> dto.patientId
                ),
                new Column<PatientDTO>("last_name",
                        (o, dto) -> { dto.lastName = (String) o;},
                        dto -> dto.lastName
                ),
                new Column<PatientDTO>("first_name",
                        (o, dto) -> { dto.firstName = (String) o;},
                        dto -> dto.firstName
                ),
                new Column<PatientDTO>("last_name_yomi",
                        (o, dto) -> { dto.lastNameYomi = (String) o;},
                        dto -> dto.lastNameYomi
                ),
                new Column<PatientDTO>("first_name_yomi",
                        (o, dto) -> { dto.firstNameYomi = (String) o; },
                        dto -> dto.firstNameYomi
                ),
                new Column<PatientDTO>("birthday",
                        (o, dto) -> { dto.birthday = o.toString(); },
                        dto -> LocalDate.parse(dto.birthday)
                ),
                new Column<PatientDTO>("sex",
                        (o, dto) -> { dto.sex = (String) o;},
                        dto -> dto.sex
                ),
                new Column<PatientDTO>("address",
                        (o, dto) -> { dto.address = (String) o;},
                        dto -> dto.address
                ),
                new Column<PatientDTO>("phone",
                        (o, dto) -> { dto.phone = (String) o;},
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
