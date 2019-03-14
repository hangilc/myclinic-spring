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

public class ShahokokuhoTableBase extends Table<ShahokokuhoDTO> {

  private static List<Column<ShahokokuhoDTO>> columns;

  static {
    columns =
        List.of(
            new Column<ShahokokuhoDTO>(
                "shahokokuho_id",
                true,
                true,
                (rs, dto) -> dto.shahokokuhoId = rs.getObject("shahokokuho_id", Integer.class),
                dto -> dto.shahokokuhoId),
            new Column<ShahokokuhoDTO>(
                "patient_id",
                false,
                false,
                (rs, dto) -> dto.patientId = rs.getObject("patient_id", Integer.class),
                dto -> dto.patientId),
            new Column<ShahokokuhoDTO>(
                "hokensha_bangou",
                false,
                false,
                (rs, dto) -> dto.hokenshaBangou = rs.getObject("hokensha_bangou", Integer.class),
                dto -> dto.hokenshaBangou),
            new Column<ShahokokuhoDTO>(
                "hihokensha_kigou",
                false,
                false,
                (rs, dto) -> dto.hihokenshaKigou = rs.getObject("hihokensha_kigou", String.class),
                dto -> dto.hihokenshaKigou),
            new Column<ShahokokuhoDTO>(
                "hihokensha_bangou",
                false,
                false,
                (rs, dto) -> dto.hihokenshaBangou = rs.getObject("hihokensha_bangou", String.class),
                dto -> dto.hihokenshaBangou),
            new Column<ShahokokuhoDTO>(
                "honnin",
                false,
                false,
                (rs, dto) -> dto.honnin = rs.getObject("honnin", Integer.class),
                dto -> dto.honnin),
            new Column<ShahokokuhoDTO>(
                "valid_from",
                false,
                false,
                (rs, dto) -> dto.validFrom = rs.getObject("valid_from", LocalDate.class).toString(),
                dto -> dto.validFrom),
            new Column<ShahokokuhoDTO>(
                "valid_upto",
                false,
                false,
                (rs, dto) ->
                    dto.validUpto =
                        TableBaseHelper.getValidUpto(rs.getObject("valid_upto", LocalDate.class)),
                dto -> dto.validUpto),
            new Column<ShahokokuhoDTO>(
                "kourei",
                false,
                false,
                (rs, dto) -> dto.kourei = rs.getObject("kourei", Integer.class),
                dto -> dto.kourei));
  }

  @Override
  protected String getTableName() {
    return "shahokokuho";
  }

  @Override
  protected Class<ShahokokuhoDTO> getClassDTO() {
    return ShahokokuhoDTO.class;
  }

  @Override
  protected List<Column<ShahokokuhoDTO>> getColumns() {
    return columns;
  }
}
