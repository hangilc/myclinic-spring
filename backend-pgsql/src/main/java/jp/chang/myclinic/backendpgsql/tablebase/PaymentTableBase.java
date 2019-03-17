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

public class PaymentTableBase extends Table<PaymentDTO> {

  private static List<Column<PaymentDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "visit_id",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.amount),
                (rs, i, dto) -> dto.amount = rs.getObject(i, Integer.class)),
            new Column<>(
                "paytime",
                true,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(i, TableBaseHelper.stringToLocalDateTime(dto.paytime)),
                (rs, i, dto) ->
                    dto.paytime =
                        TableBaseHelper.localDateTimeToString(
                            rs.getObject(i, LocalDateTime.class))));
  }

  @Override()
  protected String getTableName() {
    return "payment";
  }

  @Override()
  protected Class<PaymentDTO> getClassDTO() {
    return PaymentDTO.class;
  }

  @Override()
  protected List<Column<PaymentDTO>> getColumns() {
    return columns;
  }
}
