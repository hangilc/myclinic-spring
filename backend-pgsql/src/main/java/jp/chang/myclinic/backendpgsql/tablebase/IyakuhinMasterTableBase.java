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

public class IyakuhinMasterTableBase extends Table<IyakuhinMasterDTO> {

  private static List<Column<IyakuhinMasterDTO>> columns;

  static {
    columns =
        List.of(
            new Column<IyakuhinMasterDTO>(
                "iyakuhincode",
                true,
                false,
                (rs, dto) -> dto.iyakuhincode = rs.getObject("iyakuhincode", Integer.class),
                dto -> dto.iyakuhincode),
            new Column<IyakuhinMasterDTO>(
                "name",
                false,
                false,
                (rs, dto) -> dto.name = rs.getObject("name", String.class),
                dto -> dto.name),
            new Column<IyakuhinMasterDTO>(
                "yomi",
                false,
                false,
                (rs, dto) -> dto.yomi = rs.getObject("yomi", String.class),
                dto -> dto.yomi),
            new Column<IyakuhinMasterDTO>(
                "unit",
                false,
                false,
                (rs, dto) -> dto.unit = rs.getObject("unit", String.class),
                dto -> dto.unit),
            new Column<IyakuhinMasterDTO>(
                "yakka",
                false,
                false,
                (rs, dto) -> dto.yakka = rs.getObject("yakka", BigDecimal.class).doubleValue(),
                dto -> dto.yakka),
            new Column<IyakuhinMasterDTO>(
                "madoku",
                false,
                false,
                (rs, dto) -> dto.madoku = rs.getObject("madoku", String.class).charAt(0),
                dto -> dto.madoku),
            new Column<IyakuhinMasterDTO>(
                "kouhatsu",
                false,
                false,
                (rs, dto) -> dto.kouhatsu = rs.getObject("kouhatsu", String.class).charAt(0),
                dto -> dto.kouhatsu),
            new Column<IyakuhinMasterDTO>(
                "zaikei",
                false,
                false,
                (rs, dto) -> dto.zaikei = rs.getObject("zaikei", String.class).charAt(0),
                dto -> dto.zaikei),
            new Column<IyakuhinMasterDTO>(
                "valid_from",
                true,
                false,
                (rs, dto) -> dto.validFrom = rs.getObject("valid_from", LocalDate.class).toString(),
                dto -> dto.validFrom),
            new Column<IyakuhinMasterDTO>(
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
    return "iyakuhin_master";
  }

  @Override
  protected Class<IyakuhinMasterDTO> getClassDTO() {
    return IyakuhinMasterDTO.class;
  }

  @Override
  protected List<Column<IyakuhinMasterDTO>> getColumns() {
    return columns;
  }
}
