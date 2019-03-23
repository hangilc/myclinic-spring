package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.PaymentTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.PaymentDTO;

public class PaymentTableBase extends Table<PaymentDTO> implements PaymentTableInterface {

  public PaymentTableBase(Query query) {
    super(query);
  }

  private static List<Column<PaymentDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "visit_id",
                "visitId",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getInt(i)),
            new Column<>(
                "amount",
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.amount),
                (rs, i, dto) -> dto.amount = rs.getInt(i)),
            new Column<>(
                "paytime",
                "paytime",
                false,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(i, TableBaseHelper.stringToLocalDateTime(dto.paytime)),
                (rs, i, dto) ->
                    dto.paytime =
                        TableBaseHelper.localDateTimeToString(
                            rs.getObject(i, LocalDateTime.class))));
  }

  @Override()
  public String getTableName() {
    return "visit_payment";
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
