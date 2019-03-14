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

public class PaymentTableBase extends Table<PaymentDTO> {

  private static List<Column<PaymentDTO>> columns;

  static {
    columns =
        List.of(
            new Column<PaymentDTO>(
                "visit_id",
                true,
                false,
                (rs, dto) -> dto.visitId = rs.getObject("visit_id", Integer.class),
                dto -> dto.visitId),
            new Column<PaymentDTO>(
                "amount",
                false,
                false,
                (rs, dto) -> dto.amount = rs.getObject("amount", Integer.class),
                dto -> dto.amount),
            new Column<PaymentDTO>(
                "paytime",
                true,
                false,
                (rs, dto) ->
                    dto.paytime =
                        TableBaseHelper.localDateTimeToString(
                            rs.getObject("paytime", LocalDateTime.class)),
                dto -> dto.paytime));
  }

  @Override
  protected String getTableName() {
    return "payment";
  }

  @Override
  protected Class<PaymentDTO> getClassDTO() {
    return PaymentDTO.class;
  }

  @Override
  protected List<Column<PaymentDTO>> getColumns() {
    return columns;
  }
}
