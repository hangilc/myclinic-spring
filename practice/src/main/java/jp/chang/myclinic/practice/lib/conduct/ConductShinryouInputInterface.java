package jp.chang.myclinic.practice.lib.conduct;

import jp.chang.myclinic.dto.ConductShinryouDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;

import java.util.ArrayList;
import java.util.List;

public interface ConductShinryouInputInterface {

    int getShinryoucode();

    void setShinryoucode(int shinryoucode);
    void setName(String name);

    default void setMaster(ShinryouMasterDTO master){
        setShinryoucode(master.shinryoucode);
        setName(master.name);
    }

    default List<String> stuffInto(ConductShinryouDTO shinryou){
        List<String> err = new ArrayList<>();
        int shinryoucode = getShinryoucode();
        if( shinryoucode != 0 ){
            shinryou.shinryoucode = getShinryoucode();
        } else {
            err.add("診療行為が設定されていません。");
        }
        return err;
    }
}
