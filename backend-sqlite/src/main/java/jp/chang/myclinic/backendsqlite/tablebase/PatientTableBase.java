package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.PatientDTO;

public class PatientTableBase extends Table<PatientDTO> {

  public PatientTableBase(Query query) {
    super(query);
  }

  private static List<Column<PatientDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "patient_id",
                "patientId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getInt(i)),
            new Column<>(
                "last_name",
                "lastName",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.lastName),
                (rs, i, dto) -> dto.lastName = rs.getString(i)),
            new Column<>(
                "first_name",
                "firstName",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.firstName),
                (rs, i, dto) -> dto.firstName = rs.getString(i)),
            new Column<>(
                "last_name_yomi",
                "lastNameYomi",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.lastNameYomi),
                (rs, i, dto) -> dto.lastNameYomi = rs.getString(i)),
            new Column<>(
                "first_name_yomi",
                "firstNameYomi",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.firstNameYomi),
                (rs, i, dto) -> dto.firstNameYomi = rs.getString(i)),
            new Column<>(
                "birthday",
                "birthday",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.birthday),
                (rs, i, dto) -> dto.birthday = rs.getString(i)),
            new Column<>(
                "sex",
                "sex",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.sex),
                (rs, i, dto) -> dto.sex = rs.getString(i)),
            new Column<>(
                "address",
                "address",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.address),
                (rs, i, dto) -> dto.address = rs.getString(i)),
            new Column<>(
                "phone",
                "phone",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.phone),
                (rs, i, dto) -> dto.phone = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
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
