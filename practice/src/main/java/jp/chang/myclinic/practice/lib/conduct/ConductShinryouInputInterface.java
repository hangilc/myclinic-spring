package jp.chang.myclinic.practice.lib.conduct;

import jp.chang.myclinic.dto.ConductShinryouDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.lib.Stuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface ConductShinryouInputInterface extends Stuffer<ConductShinryouDTO> {

    int getShinryoucode();

    void setShinryoucode(int shinryoucode);
    void setName(String name);

    default void setMaster(ShinryouMasterDTO master){
        setShinryoucode(master.shinryoucode);
        setName(master.name);
    }

    @Override
    default void stuffInto(ConductShinryouDTO shinryou, Consumer<ConductShinryouDTO> okHandler, Consumer<List<String>> errorHandler){
        List<String> err = new ArrayList<>();
        int shinryoucode = getShinryoucode();
        if( shinryoucode != 0 ){
            shinryou.shinryoucode = getShinryoucode();
        } else {
            err.add("診療行為が設定されていません。");
        }
        if( err.size() > 0 ){
            errorHandler.accept(err);
        } else {
            okHandler.accept(shinryou);
        }
    }

}
