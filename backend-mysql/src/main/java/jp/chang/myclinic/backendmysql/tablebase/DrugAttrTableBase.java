package jp.chang.myclinic.backendmysql.tablebase;

import jp.chang.myclinic.backenddb.Column;
import jp.chang.myclinic.backenddb.Table;
import jp.chang.myclinic.backenddb.Query;
import jp.chang.myclinic.backenddb.TableBaseHelper;
import jp.chang.myclinic.backenddb.tableinterface.DrugAttrTableInterface;
import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import jp.chang.myclinic.dto.DrugAttrDTO;

public class DrugAttrTableBase extends Table<DrugAttrDTO> implements DrugAttrTableInterface {

  public DrugAttrTableBase(Query query) {
    super(query);
  }

  private static List<Column<DrugAttrDTO>> columns;

  static {
    columns =
        List.of(
            new Column<>(
                "drug_id",
                "drugId",
                true,
                false,
                (stmt, i, dto) -> stmt.setInt(i, dto.drugId),
                (rs, i, dto) -> dto.drugId = rs.getInt(i)),
            new Column<>(
                "tekiyou",
                "tekiyou",
                false,
                false,
                (stmt, i, dto) -> stmt.setString(i, dto.tekiyou),
                (rs, i, dto) -> dto.tekiyou = rs.getString(i)));
  }

  @Override()
  public String getTableName() {
    return "drug_attr";
  }

  @Override()
  protected Class<DrugAttrDTO> getClassDTO() {
    return DrugAttrDTO.class;
  }

  @Override()
  protected List<Column<DrugAttrDTO>> getColumns() {
    return columns;
  }
}
