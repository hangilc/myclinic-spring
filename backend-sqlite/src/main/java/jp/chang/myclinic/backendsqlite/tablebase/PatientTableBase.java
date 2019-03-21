package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.PatientTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.PatientDTO;

public class PatientTableBase extends Table<PatientDTO> implements PatientTableInterface {

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
                (rs, i, dto) -> dto.patientId = rs.getObject(i, Integer.class)),
            new Column<>(
                "last_name",
                "lastName",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.lastName),
                (rs, i, dto) -> dto.lastName = rs.getObject(i, String.class)),
            new Column<>(
                "first_name",
                "firstName",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.firstName),
                (rs, i, dto) -> dto.firstName = rs.getObject(i, String.class)),
            new Column<>(
                "last_name_yomi",
                "lastNameYomi",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.lastNameYomi),
                (rs, i, dto) -> dto.lastNameYomi = rs.getObject(i, String.class)),
            new Column<>(
                "first_name_yomi",
                "firstNameYomi",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.firstNameYomi),
                (rs, i, dto) -> dto.firstNameYomi = rs.getObject(i, String.class)),
            new Column<>(
                "birthday",
                "birthday",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.birthday),
                (rs, i, dto) -> dto.birthday = rs.getObject(i, String.class)),
            new Column<>(
                "sex",
                "sex",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.sex),
                (rs, i, dto) -> dto.sex = rs.getObject(i, String.class)),
            new Column<>(
                "address",
                "address",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.address),
                (rs, i, dto) -> dto.address = rs.getObject(i, String.class)),
            new Column<>(
                "phone",
                "phone",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.phone),
                (rs, i, dto) -> dto.phone = rs.getObject(i, String.class)));
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
