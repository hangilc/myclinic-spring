package jp.chang.myclinic.apitool.databasespecifics;

import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.types.ShinryouTensuu;
import jp.chang.myclinic.apitool.types.ValidUptoDate;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MysqlSpecifics implements DatabaseSpecifics {

    private Helper helper = Helper.getInstance();

    @Override
    public String getCatalog() {
        return "myclinic";
    }

    @Override
    public String getSchema() {
        return null;
    }

    private static final Map<Class<?>, String> dtoClassToTableNameMap = new HashMap<>();

    static {
        dtoClassToTableNameMap.put(ByoumeiMasterDTO.class, "shoubyoumei_master_arch");
        dtoClassToTableNameMap.put(ChargeDTO.class, "visit_charge");
        dtoClassToTableNameMap.put(ConductDTO.class, "visit_conduct");
        dtoClassToTableNameMap.put(ConductDrugDTO.class, "visit_conduct_drug");
        dtoClassToTableNameMap.put(ConductKizaiDTO.class, "visit_conduct_kizai");
        dtoClassToTableNameMap.put(ConductShinryouDTO.class, "visit_conduct_shinryou");
        dtoClassToTableNameMap.put(DiseaseDTO.class, "disease");
        dtoClassToTableNameMap.put(DiseaseAdjDTO.class, "disease_adj");
        dtoClassToTableNameMap.put(DrugDTO.class, "visit_drug");
        dtoClassToTableNameMap.put(DrugAttrDTO.class, "drug_attr");
        dtoClassToTableNameMap.put(GazouLabelDTO.class, "visit_gazou_label");
        dtoClassToTableNameMap.put(HotlineDTO.class, "hotline");
        dtoClassToTableNameMap.put(IntraclinicCommentDTO.class, "intraclinic_comment");
        dtoClassToTableNameMap.put(IntraclinicPostDTO.class, "intraclinic_post");
        dtoClassToTableNameMap.put(IntraclinicTagDTO.class, "intraclinic_tag");
        dtoClassToTableNameMap.put(IntraclinicTagPostDTO.class, "intraclinic_tag_post");
        dtoClassToTableNameMap.put(IyakuhinMasterDTO.class, "iyakuhin_master_arch");
        dtoClassToTableNameMap.put(KizaiMasterDTO.class, "tokuteikizai_master_arch");
        dtoClassToTableNameMap.put(KouhiDTO.class, "kouhi");
        dtoClassToTableNameMap.put(KoukikoureiDTO.class, "hoken_koukikourei");
        dtoClassToTableNameMap.put(PatientDTO.class, "patient");
        dtoClassToTableNameMap.put(PaymentDTO.class, "visit_payment");
        dtoClassToTableNameMap.put(PharmaDrugDTO.class, "pharma_drug");
        dtoClassToTableNameMap.put(PharmaQueueDTO.class, "pharma_queue");
        dtoClassToTableNameMap.put(PracticeLogDTO.class, "practice_log");
        dtoClassToTableNameMap.put(PrescExampleDTO.class, "presc_example");
        dtoClassToTableNameMap.put(RoujinDTO.class, "hoken_roujin");
        dtoClassToTableNameMap.put(ShahokokuhoDTO.class, "hoken_shahokokuho");
        dtoClassToTableNameMap.put(ShinryouDTO.class, "visit_shinryou");
        dtoClassToTableNameMap.put(ShinryouAttrDTO.class, "shinryou_attr");
        dtoClassToTableNameMap.put(ShinryouMasterDTO.class, "shinryoukoui_master_arch");
        dtoClassToTableNameMap.put(ShoukiDTO.class, "shouki");
        dtoClassToTableNameMap.put(ShuushokugoMasterDTO.class, "shuushokugo_master");
        dtoClassToTableNameMap.put(TextDTO.class, "visit_text");
        dtoClassToTableNameMap.put(VisitDTO.class, "visit");
        dtoClassToTableNameMap.put(WqueueDTO.class, "wqueue");
    }

    @Override
    public String dtoClassToDbTableName(Class<?> dtoClass) {
        return dtoClassToTableNameMap.computeIfAbsent(dtoClass, key -> {
            String msg = String.format("Cannot convert DTO type (%s) to table name.", dtoClass.getSimpleName());
            throw new RuntimeException(msg);
        });
    }

    @Override
    public String getDtoFieldName(String table, String dbColumnName) {
        switch (table) {
            case "visit_conduct": {
                switch (dbColumnName) {
                    case "id":
                        return "conductId";
                }
                break;
            }
            case "visit_conduct_drug": {
                switch (dbColumnName) {
                    case "id":
                        return "conductDrugId";
                    case "visit_conduct_id":
                        return "conductId";
                }
                break;
            }
            case "visit_conduct_kizai": {
                switch (dbColumnName) {
                    case "id":
                        return "conductKizaiId";
                    case "visit_conduct_id":
                        return "conductId";
                }
                break;
            }
            case "visit_conduct_shinryou": {
                switch (dbColumnName) {
                    case "id":
                        return "conductShinryouId";
                    case "visit_conduct_id":
                        return "conductId";
                }
                break;
            }
            case "visit_drug": {
                return helper.snakeToCamel(dbColumnName.replaceAll("^d_", ""));
            }
            case "visit_gazou_label": {
                switch(dbColumnName){
                    case "visit_conduct_id": return "conductId";
                }
                break;
            }
            case "hotline": {
                switch(dbColumnName){
                    case "m_datetime": return "postedAt";
                }
                break;
            }
            case "intraclinic_tag": {
                switch(dbColumnName){
                    case "id": return "tagId";
                }
                break;
            }
            case "iyakuhin_master_arch": {
                switch(dbColumnName){
                    case "yakkacode": return null;
                }
                break;
            }
            case "patient": {
                switch(dbColumnName){
                    case "birth_day": return "birthday";
                }
                break;
            }
            case "practice_log": {
                switch(dbColumnName){
                    case "practice_log_id": return "serialId";
                }
                break;
            }
            case "presc_example": {
                return helper.snakeToCamel(dbColumnName.replaceAll("^m_", ""));
            }
            case "shinryoukoui_master_arch": {
                switch(dbColumnName){
                    case "kensagroup": return "kensaGroup";
                }
                break;
            }
            case "visit": {
                switch(dbColumnName){
                    case "v_datetime": return "visitedAt";
                }
                break;
            }
        }
        return helper.snakeToCamel(dbColumnName);
    }

    @Override
    public Class<?> getDbColumnClass(String tableName, String columnName, int sqlType, String dbTypeName) {
        if( sqlType == Types.DATE ){
            if( columnName.equals("valid_upto") ||
                    (tableName.equals("disease") && columnName.equals("end_date"))){
                return ValidUptoDate.class;
            } else {
                return LocalDate.class;
            }
        }
        if( tableName.equals("shinryoukoui_master_arch") && columnName.equals("tensuu") ){
            return ShinryouTensuu.class;
        }
        switch(sqlType){
            case Types.INTEGER: case Types.TINYINT:
                return Integer.class;
            case Types.DOUBLE: return Double.class;
            case Types.VARCHAR: case Types.LONGVARCHAR: case Types.CHAR:
                return String.class;
            case Types.TIMESTAMP: return LocalDateTime.class;
            default: return null;
        }
    }
}
