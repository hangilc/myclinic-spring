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

public class KizaiMasterTableBase extends Table<KizaiMasterDTO> {

  private static List<Column<KizaiMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "kizaicode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.kizaicode),
                (rs, i, dto) -> dto.kizaicode = rs.getObject(i, Integer.class)),
            new Column<>(
                "name",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.name),
                (rs, i, dto) -> dto.name = rs.getObject(i, String.class)),
            new Column<>(
                "yomi",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.yomi),
                (rs, i, dto) -> dto.yomi = rs.getObject(i, String.class)),
            new Column<>(
                "unit",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.unit),
                (rs, i, dto) -> dto.unit = rs.getObject(i, String.class)),
            new Column<>(
                "kingaku",
                false,
                false,
                (stmt, i, dto) -> stmt.setBigDecimal(i, new BigDecimal(dto.kingaku)),
                (rs, i, dto) -> dto.kingaku = rs.getObject(i, BigDecimal.class).doubleValue()),
            new Column<>(
                "valid_from",
                true,
                false,
                (stmt, i, dto) -> stmt.setObject(i, LocalDate.parse(dto.validFrom)),
                (rs, i, dto) -> dto.validFrom = rs.getObject(i, LocalDate.class).toString()),
            new Column<>(
                "valid_upto",
                false,
                false,
                (stmt, i, dto) ->
                    stmt.setObject(
                        i, TableBaseHelper.validUptoFromStringToLocalDate(dto.validUpto)),
                (rs, i, dto) ->
                    dto.validUpto =
                        TableBaseHelper.validUptoFromLocalDateToString(
                            rs.getObject(i, LocalDate.class))));
  }

  @Override()
  protected String getTableName() {
    return "kizai_master";
  }

  @Override()
  protected Class<KizaiMasterDTO> getClassDTO() {
    return KizaiMasterDTO.class;
  }

  @Override()
  protected List<Column<KizaiMasterDTO>> getColumns() {
    return columns;
  }
}
