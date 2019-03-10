package jp.chang.myclinic.backendpgsql.table;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.dto.PatientDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PatientTable extends Table<PatientDTO> {

    @Override
    protected String getTableName() {
        return "patient";
    }

    @Override
    protected PatientDTO newInstanceDTO() {
        return new PatientDTO();
    }

    public PatientTable(){
        addColumn(new Column<PatientDTO>("patient_id")
                .isPrimary(true)
                .isAutoIncrement(true)
                .putIntoDTO((o, dto) -> dto.patientId = (Integer)o)
                .getFromDTO(dto -> dto.patientId)
        );
        addColumn(new Column<PatientDTO>("last_name")
                .putIntoDTO((o, dto) -> dto.lastName = (String)o)
                .getFromDTO(dto -> dto.lastName)
        );
        addColumn(new Column<PatientDTO>("first_name")
                .putIntoDTO((o, dto) -> dto.firstName = (String)o)
                .getFromDTO(dto -> dto.firstName)
        );
        addColumn(new Column<PatientDTO>("last_name_yomi")
                .putIntoDTO((o, dto) -> dto.lastNameYomi = (String)o)
                .getFromDTO(dto -> dto.lastNameYomi)
        );
        addColumn(new Column<PatientDTO>("first_name_yomi")
                .putIntoDTO((o, dto) -> dto.firstNameYomi = (String)o)
                .getFromDTO(dto -> dto.firstNameYomi)
        );
        addColumn(new Column<PatientDTO>("birthday")
                .putIntoDTO((o, dto) -> dto.birthday = o.toString())
                .getFromDTO(dto -> LocalDate.parse(dto.birthday))
        );
        addColumn(new Column<PatientDTO>("sex")
                .putIntoDTO((o, dto) -> dto.sex = (String)o)
                .getFromDTO(dto -> dto.sex)
        );
        addColumn(new Column<PatientDTO>("address")
                .putIntoDTO((o, dto) -> dto.address = (String)o)
                .getFromDTO(dto -> dto.address)
        );
        addColumn(new Column<PatientDTO>("phone")
                .putIntoDTO((o, dto) -> dto.phone = (String)o)
                .getFromDTO(dto -> dto.phone)
        );
    }

}
