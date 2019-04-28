package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.NotInDatabase;

@NotInDatabase
public class ShinryouWithAttrDTO {

    public ShinryouDTO shinryou;
    public ShinryouAttrDTO attr;

    public static ShinryouWithAttrDTO create(ShinryouDTO shinryou, ShinryouAttrDTO attr){
        ShinryouWithAttrDTO result = new ShinryouWithAttrDTO();
        result.shinryou = shinryou;
        result.attr = attr;
        return result;
    }

    @Override
    public String toString() {
        return "ShinryouWithAttrDTO{" +
                "shinryou=" + shinryou +
                ", attr=" + attr +
                '}';
    }
}
