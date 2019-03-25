package jp.chang.myclinic.backenddb;

public interface TableBaseInterface<DTO> extends
        TableInterface<DTO>,
        Query.Projector<DTO>,
        SqlTranslator.TableInfo {
}
