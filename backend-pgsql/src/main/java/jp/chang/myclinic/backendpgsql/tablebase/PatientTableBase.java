package jp.chang.myclinic.backendpgsql.tablebase;

import jp.chang.myclinic.backendpgsql.Column;
import jp.chang.myclinic.backendpgsql.Table;
import jp.chang.myclinic.backendpgsql.tablebasehelper.TableBaseHelper;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.*;
import java.sql.Connection;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;

public class PatientTableBase extends Table<PatientDTO> {

  private static List<Column<PatientDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "patient_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getObject(i, Integer.class)),
            new Column<>(
                "last_name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.lastName),
                (rs, i, dto) -> dto.lastName = rs.getObject(i, String.class)),
            new Column<>(
                "first_name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.firstName),
                (rs, i, dto) -> dto.firstName = rs.getObject(i, String.class)),
            new Column<>(
                "last_name_yomi",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.lastNameYomi),
                (rs, i, dto) -> dto.lastNameYomi = rs.getObject(i, String.class)),
            new Column<>(
                "first_name_yomi",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.firstNameYomi),
                (rs, i, dto) -> dto.firstNameYomi = rs.getObject(i, String.class)),
            new Column<>(
                "sex",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.sex),
                (rs, i, dto) -> dto.sex = rs.getObject(i, String.class)),
            new Column<>(
                "birthday",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.birthday)),
                (rs, i, dto) -> dto.birthday = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "address",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.address),
                (rs, i, dto) -> dto.address = rs.getObject(i, String.class)),
            new Column<>(
                "phone",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.phone),
                (rs, i, dto) -> dto.phone = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
    return "patient";
  }

  @Override()
  protected Class<PatientDTO> getClassDTO() {
    return PatientDTO.class;
  }

  @Override()
  protected List<Column<PatientDTO>> getColumns() {
    return columns;
  }
}
