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

public class RoujinTableBase extends Table<RoujinDTO> {

  private static List<Column<RoujinDTO>> columns;

  static {
    columns =
        List.of(
            new Column<RoujinDTO>(
                "roujin_id",
                true,
                true,
                (rs, dto) -> dto.roujinId = rs.getObject("roujin_id", Integer.class),
                dto -> dto.roujinId),
            new Column<RoujinDTO>(
                "patient_id",
                false,
                false,
                (rs, dto) -> dto.patientId = rs.getObject("patient_id", Integer.class),
                dto -> dto.patientId),
            new Column<RoujinDTO>(
                "shichouson",
                false,
                false,
                (rs, dto) -> dto.shichouson = rs.getObject("shichouson", Integer.class),
                dto -> dto.shichouson),
            new Column<RoujinDTO>(
                "jukyuusha",
                false,
                false,
                (rs, dto) -> dto.jukyuusha = rs.getObject("jukyuusha", Integer.class),
                dto -> dto.jukyuusha),
            new Column<RoujinDTO>(
                "futan_wari",
                false,
                false,
                (rs, dto) -> dto.futanWari = rs.getObject("futan_wari", Integer.class),
                dto -> dto.futanWari),
            new Column<RoujinDTO>(
                "valid_from",
                false,
                false,
                (rs, dto) -> dto.validFrom = rs.getObject("valid_from", LocalDate.class).toString(),
                dto -> dto.validFrom),
            new Column<RoujinDTO>(
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
    return "roujin";
  }

  @Override
  protected Class<RoujinDTO> getClassDTO() {
    return RoujinDTO.class;
  }

  @Override
  protected List<Column<RoujinDTO>> getColumns() {
    return columns;
  }
}
