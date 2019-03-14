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

public class KoukikoureiTableBase extends Table<KoukikoureiDTO> {

  private static List<Column<KoukikoureiDTO>> columns;

  static {
    columns =
        List.of(
            new Column<KoukikoureiDTO>(
                "koukikourei_id",
                true,
                true,
                (rs, dto) -> dto.koukikoureiId = rs.getObject("koukikourei_id", Integer.class),
                dto -> dto.koukikoureiId),
            new Column<KoukikoureiDTO>(
                "patient_id",
                false,
                false,
                (rs, dto) -> dto.patientId = rs.getObject("patient_id", Integer.class),
                dto -> dto.patientId),
            new Column<KoukikoureiDTO>(
                "hokensha_bangou",
                false,
                false,
                (rs, dto) ->
                    dto.hokenshaBangou = rs.getObject("hokensha_bangou", Integer.class).toString(),
                dto -> dto.hokenshaBangou),
            new Column<KoukikoureiDTO>(
                "hihokensha_bangou",
                false,
                false,
                (rs, dto) ->
                    dto.hihokenshaBangou =
                        rs.getObject("hihokensha_bangou", Integer.class).toString(),
                dto -> dto.hihokenshaBangou),
            new Column<KoukikoureiDTO>(
                "futan_wari",
                false,
                false,
                (rs, dto) -> dto.futanWari = rs.getObject("futan_wari", Integer.class),
                dto -> dto.futanWari),
            new Column<KoukikoureiDTO>(
                "valid_from",
                false,
                false,
                (rs, dto) -> dto.validFrom = rs.getObject("valid_from", LocalDate.class).toString(),
                dto -> dto.validFrom),
            new Column<KoukikoureiDTO>(
                "valid_upto",
                false,
                false,
                (rs, dto) ->
                    dto.validUpto =
                        TableBaseHelper.getValidUpto(rs.getObject("valid_upto", LocalDate.class)),
                dto -> dto.validUpto));
  }

  @Override
  protected String getTableName() {
    return "koukikourei";
  }

  @Override
  protected Class<KoukikoureiDTO> getClassDTO() {
    return KoukikoureiDTO.class;
  }

  @Override
  protected List<Column<KoukikoureiDTO>> getColumns() {
    return columns;
  }
}
