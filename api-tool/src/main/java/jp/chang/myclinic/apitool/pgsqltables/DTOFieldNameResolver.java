package jp.chang.myclinic.apitool.pgsqltables;

import jp.chang.myclinic.apitool.lib.Helper;

import java.util.HashMap;
import java.util.Map;

class DTOFieldNameResolver {

    private static Map<String, String> fieldMap = new HashMap<>();

    static {
        fieldMap.put("pharma_drug:side_effect", "sideeffect");
    }

    private static Helper helper = new Helper();

    static String resolve(String table, String sqlColumnName){
        String ex = fieldMap.getOrDefault(table + ":" + sqlColumnName, null);
        return ex != null ? ex : helper.snakeToCamel(sqlColumnName);
    }

}
