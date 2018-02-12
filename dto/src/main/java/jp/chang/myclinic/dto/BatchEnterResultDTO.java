package jp.chang.myclinic.dto;

import java.util.List;

public class BatchEnterResultDTO {
    public List<Integer> shinryouIds;
    public List<Integer> conductIds;

    public static void assign(BatchEnterResultDTO dst, BatchEnterResultDTO src){
        dst.shinryouIds = src.shinryouIds;
        dst.conductIds = src.conductIds;
    }
}
