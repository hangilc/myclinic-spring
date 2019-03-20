package jp.chang.myclinic.backendsqlite.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.PrescExampleTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.PrescExampleDTO;

public class PrescExampleTableBase extends Table<PrescExampleDTO>
    implements PrescExampleTableInterface {

  private static List<Column<PrescExampleDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "presc_example_id",
                "prescExampleId",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.prescExampleId),
                (rs, i, dto) -> dto.prescExampleId = rs.getObject(i, Integer.class)),
            new Column<>(
                "iyakuhincode",
                "iyakuhincode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
            new Column<>(
                "master_valid_from",
                "masterValidFrom",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.masterValidFrom),
                (rs, i, dto) -> dto.masterValidFrom = rs.getObject(i, String.class)),
            new Column<>(
                "amount",
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.amount),
                (rs, i, dto) -> dto.amount = rs.getObject(i, String.class)),
            new Column<>(
                "usage",
                "usage",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.usage),
                (rs, i, dto) -> dto.usage = rs.getObject(i, String.class)),
            new Column<>(
                "days",
                "days",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.days),
                (rs, i, dto) -> dto.days = rs.getObject(i, Integer.class)),
            new Column<>(
                "category",
                "category",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.category),
                (rs, i, dto) -> dto.category = rs.getObject(i, Integer.class)),
            new Column<>(
                "comment",
                "comment",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.comment),
                (rs, i, dto) -> dto.comment = rs.getObject(i, String.class)));
  }

  @Override()
  public String getTableName() {
    return "presc_example";
  }

  @Override()
  protected Class<PrescExampleDTO> getClassDTO() {
    return PrescExampleDTO.class;
  }

  @Override()
  protected List<Column<PrescExampleDTO>> getColumns() {
    return columns;
  }
}
