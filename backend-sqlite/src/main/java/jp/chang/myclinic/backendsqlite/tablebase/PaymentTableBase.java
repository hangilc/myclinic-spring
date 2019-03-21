package jp.chang.myclinic.backendsqlite.tablebase;

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
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.visitId),
                (rs, i, dto) -> dto.visitId = rs.getObject(i, Integer.class)),
            new Column<>(
                "amount",
                "amount",
                false,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.amount),
                (rs, i, dto) -> dto.amount = rs.getObject(i, Integer.class)),
            new Column<>(
                "paytime",
                "paytime",
                true,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.paytime),
                (rs, i, dto) -> dto.paytime = rs.getObject(i, String.class)));
  }

  @Override()
  public String getTableName() {
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
