package jp.chang.myclinic.backendpgsql.tablebase;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.*;
import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.backendpgsql.tablebasehelper.TableBaseHelper;

public class PatientTableBase extends Table<PatientDTO> {

  private static List<Column<PatientDTO>> columns;

  static {
    columns =
        List.of(
            new Column<PatientDTO>(
                "patient_id",
                true,
                true,
                (rs, dto) -> dto.patientId = rs.getObject("patient_id", Integer.class),
                dto -> dto.patientId),
            new Column<PatientDTO>(
                "last_name",
                false,
                false,
                (rs, dto) -> dto.lastName = rs.getObject("last_name", String.class),
                dto -> dto.lastName),
            new Column<PatientDTO>(
                "first_name",
                false,
                false,
                (rs, dto) -> dto.firstName = rs.getObject("first_name", String.class),
                dto -> dto.firstName),
            new Column<PatientDTO>(
                "last_name_yomi",
                false,
                false,
                (rs, dto) -> dto.lastNameYomi = rs.getObject("last_name_yomi", String.class),
                dto -> dto.lastNameYomi),
            new Column<PatientDTO>(
                "first_name_yomi",
                false,
                false,
                (rs, dto) -> dto.firstNameYomi = rs.getObject("first_name_yomi", String.class),
                dto -> dto.firstNameYomi),
            new Column<PatientDTO>(
                "sex",
                false,
                false,
                (rs, dto) -> dto.sex = rs.getObject("sex", String.class),
                dto -> dto.sex),
            new Column<PatientDTO>(
                "birthday",
                false,
                false,
                (rs, dto) -> dto.birthday = rs.getObject("birthday", LocalDate.class).toString(),
                dto -> dto.birthday),
            new Column<PatientDTO>(
                "address",
                false,
                false,
                (rs, dto) -> dto.address = rs.getObject("address", String.class),
                dto -> dto.address),
            new Column<PatientDTO>(
                "phone",
                false,
                false,
                (rs, dto) -> dto.phone = rs.getObject("phone", String.class),
                dto -> dto.phone));
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
