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

public class KizaiMasterTableBase extends Table<KizaiMasterDTO> {

  private static List<Column<KizaiMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<KizaiMasterDTO>(
                "kizaicode",
                true,
                false,
                (rs, dto) -> dto.kizaicode = rs.getObject("kizaicode", Integer.class),
                dto -> dto.kizaicode),
            new Column<KizaiMasterDTO>(
                "name",
                false,
                false,
                (rs, dto) -> dto.name = rs.getObject("name", String.class),
                dto -> dto.name),
            new Column<KizaiMasterDTO>(
                "yomi",
                false,
                false,
                (rs, dto) -> dto.yomi = rs.getObject("yomi", String.class),
                dto -> dto.yomi),
            new Column<KizaiMasterDTO>(
                "unit",
                false,
                false,
                (rs, dto) -> dto.unit = rs.getObject("unit", String.class),
                dto -> dto.unit),
            new Column<KizaiMasterDTO>(
                "kingaku",
                false,
                false,
                (rs, dto) -> dto.kingaku = rs.getObject("kingaku", BigDecimal.class).doubleValue(),
                dto -> dto.kingaku),
            new Column<KizaiMasterDTO>(
                "valid_from",
                true,
                false,
                (rs, dto) -> dto.validFrom = rs.getObject("valid_from", LocalDate.class).toString(),
                dto -> dto.validFrom),
            new Column<KizaiMasterDTO>(
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
    return "kizai_master";
  }

  @Override
  protected Class<KizaiMasterDTO> getClassDTO() {
    return KizaiMasterDTO.class;
  }

  @Override
  protected List<Column<KizaiMasterDTO>> getColumns() {
    return columns;
  }
}
