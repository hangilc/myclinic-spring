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

public class PrescExampleTableBase extends Table<PrescExampleDTO> {

  private static List<Column<PrescExampleDTO>> columns;

  static {
    columns =
        List.of(
            new Column<PrescExampleDTO>(
                "presc_example_id",
                true,
                true,
                (rs, dto) -> dto.prescExampleId = rs.getObject("presc_example_id", Integer.class),
                dto -> dto.prescExampleId),
            new Column<PrescExampleDTO>(
                "iyakuhincode",
                false,
                false,
                (rs, dto) -> dto.iyakuhincode = rs.getObject("iyakuhincode", Integer.class),
                dto -> dto.iyakuhincode),
            new Column<PrescExampleDTO>(
                "master_valid_from",
                false,
                false,
                (rs, dto) ->
                    dto.masterValidFrom =
                        rs.getObject("master_valid_from", LocalDate.class).toString(),
                dto -> dto.masterValidFrom),
            new Column<PrescExampleDTO>(
                "amount",
                false,
                false,
                (rs, dto) -> dto.amount = rs.getObject("amount", BigDecimal.class).toString(),
                dto -> dto.amount),
            new Column<PrescExampleDTO>(
                "usage",
                false,
                false,
                (rs, dto) -> dto.usage = rs.getObject("usage", String.class),
                dto -> dto.usage),
            new Column<PrescExampleDTO>(
                "days",
                false,
                false,
                (rs, dto) -> dto.days = rs.getObject("days", Integer.class),
                dto -> dto.days),
            new Column<PrescExampleDTO>(
                "category",
                false,
                false,
                (rs, dto) -> dto.category = rs.getObject("category", Integer.class),
                dto -> dto.category),
            new Column<PrescExampleDTO>(
                "comment",
                false,
                false,
                (rs, dto) -> dto.comment = rs.getObject("comment", String.class),
                dto -> dto.comment));
  }

  @Override
  protected String getTableName() {
    return "presc_example";
  }

  @Override
  protected Class<PrescExampleDTO> getClassDTO() {
    return PrescExampleDTO.class;
  }

  @Override
  protected List<Column<PrescExampleDTO>> getColumns() {
    return columns;
  }
}
