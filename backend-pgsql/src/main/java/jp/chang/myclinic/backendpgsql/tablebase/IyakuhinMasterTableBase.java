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

public class IyakuhinMasterTableBase extends Table<IyakuhinMasterDTO> {

  private static List<Column<IyakuhinMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "iyakuhincode",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.iyakuhincode),
                (rs, i, dto) -> dto.iyakuhincode = rs.getObject(i, Integer.class)),
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
                "yakka",
                false,
                false,
                (stmt, i, dto) -> stmt.setBigDecimal(i, new BigDecimal(dto.yakka)),
                (rs, i, dto) -> dto.yakka = rs.getObject(i, BigDecimal.class).doubleValue()),
            new Column<>(
                "madoku",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.madoku)),
                (rs, i, dto) -> dto.madoku = rs.getObject(i, String.class).charAt(0)),
            new Column<>(
                "kouhatsu",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.kouhatsu)),
                (rs, i, dto) -> dto.kouhatsu = rs.getObject(i, String.class).charAt(0)),
            new Column<>(
                "zaikei",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, String.valueOf(dto.zaikei)),
                (rs, i, dto) -> dto.zaikei = rs.getObject(i, String.class).charAt(0)),
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
    return "iyakuhin_master";
  }

  @Override()
  protected Class<IyakuhinMasterDTO> getClassDTO() {
    return IyakuhinMasterDTO.class;
  }

  @Override()
  protected List<Column<IyakuhinMasterDTO>> getColumns() {
    return columns;
  }
}
