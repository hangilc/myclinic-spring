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

public class PrescExampleTableBase extends Table<PrescExampleDTO> {

  private static List<Column<PrescExampleDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "presc_example_id",
                true,
                true,
                (stmt, i, dto) -> stmt.setInt(i, dto.prescExampleId),
                (rs, i, dto) -> dto.prescExampleId = rs.getObject(i, Integer.class)),
            new Column<>(
                "iyakuhincode",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
            new Column<>(
                "master_valid_from",
                false,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.masterValidFrom)),
                (rs, i, dto) -> dto.masterValidFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setBigDecimal(i, new BigDecimal(dto.amount)),
                (rs, i, dto) -> dto.amount = rs.getObject(i, BigDecimal.class).toString()),
            new Column<>(
                "usage",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.usage),
                (rs, i, dto) -> dto.usage = rs.getObject(i, String.class)),
            new Column<>(
                "days",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.days),
                (rs, i, dto) -> dto.days = rs.getObject(i, Integer.class)),
            new Column<>(
                "category",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.category),
                (rs, i, dto) -> dto.category = rs.getObject(i, Integer.class)),
            new Column<>(
                "comment",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.comment),
                (rs, i, dto) -> dto.comment = rs.getObject(i, String.class)));
  }

  @Override()
  protected String getTableName() {
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
