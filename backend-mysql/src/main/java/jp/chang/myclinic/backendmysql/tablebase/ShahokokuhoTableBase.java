package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.ShahokokuhoTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.ShahokokuhoDTO;

public class ShahokokuhoTableBase extends Table<ShahokokuhoDTO>
    implements ShahokokuhoTableInterface {

  public ShahokokuhoTableBase(Query query) {
    super(query);
  }

  private static List<Column<ShahokokuhoDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "shahokokuho_id",
                "shahokokuhoId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.shahokokuhoId),
                (rs, i, dto) -> dto.shahokokuhoId = rs.getInt(i)),
            new Column<>(
                "patient_id",
                "patientId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.patientId),
                (rs, i, dto) -> dto.patientId = rs.getInt(i)),
            new Column<>(
                "hokensha_bangou",
                "hokenshaBangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.hokenshaBangou),
                (rs, i, dto) -> dto.hokenshaBangou = rs.getInt(i)),
            new Column<>(
                "hihokensha_kigou",
                "hihokenshaKigou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.hihokenshaKigou),
                (rs, i, dto) -> dto.hihokenshaKigou = rs.getString(i)),
            new Column<>(
                "hihokensha_bangou",
                "hihokenshaBangou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.hihokenshaBangou),
                (rs, i, dto) -> dto.hihokenshaBangou = rs.getString(i)),
            new Column<>(
                "honnin",
                "honnin",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.honnin),
                (rs, i, dto) -> dto.honnin = rs.getInt(i)),
            new Column<>(
                "valid_from",
                "validFrom",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.validFrom)),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "valid_upto",
                "validUpto",
                false,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(
                        i, TableBaseHelper.validUptoFromStringToLocalDate(dto.validUpto)),
                (rs, i, dto) ->
                    dto.validUpto =
                        TableBaseHelper.validUptoFromLocalDateToString(
                            rs.getObject(i, LocalDate.class))),
            new Column<>(
                "kourei",
                "kourei",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kourei),
                (rs, i, dto) -> dto.kourei = rs.getInt(i)));
  }

  @Override()
  public String getTableName() {
    return "hoken_shahokokuho";
  }

  @Override()
  protected Class<ShahokokuhoDTO> getClassDTO() {
    return ShahokokuhoDTO.class;
  }

  @Override()
  protected List<Column<ShahokokuhoDTO>> getColumns() {
    return columns;
  }
}
