package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.PrescExampleDTO;

public class PrescExampleTableBase extends Table<PrescExampleDTO> {

  public PrescExampleTableBase(Query query) {
    super(query);
  }

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
                (rs, i, dto) -> dto.prescExampleId = rs.getInt(i)),
            new Column<>(
                "m_iyakuhincode",
                "iyakuhincode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getInt(i)),
            new Column<>(
                "m_master_valid_from",
                "masterValidFrom",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.masterValidFrom)),
                (rs, i, dto) -> dto.masterValidFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "m_amount",
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.amount),
                (rs, i, dto) -> dto.amount = rs.getString(i)),
            new Column<>(
                "m_usage",
                "usage",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.usage),
                (rs, i, dto) -> dto.usage = rs.getString(i)),
            new Column<>(
                "m_days",
                "days",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.days),
                (rs, i, dto) -> dto.days = rs.getInt(i)),
            new Column<>(
                "m_category",
                "category",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.category),
                (rs, i, dto) -> dto.category = rs.getInt(i)),
            new Column<>(
                "m_comment",
                "comment",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.comment),
                (rs, i, dto) -> dto.comment = rs.getString(i)));
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
