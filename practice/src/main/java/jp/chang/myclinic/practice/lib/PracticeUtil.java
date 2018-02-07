package jp.chang.myclinic.practice.lib;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeEnv;

import java.util.List;

public class PracticeUtil {

    public static void storeKouhiList(VisitDTO visit, List<KouhiDTO> kouhiList){
        visit.kouhi1Id = 0;
        visit.kouhi2Id = 0;
        visit.kouhi3Id = 0;
        if( kouhiList.size() > 0 ){
            visit.kouhi1Id = kouhiList.get(0).kouhiId;
            if( kouhiList.size() > 1 ){
                visit.kouhi2Id = kouhiList.get(1).kouhiId;
                if( kouhiList.size() > 2 ){
                    visit.kouhi3Id = kouhiList.get(2).kouhiId;
                }
            }
        }
    }

    public static DrugCategory zaikeiToCategory(char zaikei){
        if( zaikei == MyclinicConsts.ZaikeiGaiyou ){
            return DrugCategory.Gaiyou;
        } else {
            return DrugCategory.Naifuku;
        }
    }

    public static int findCopyTarget(){
        PracticeEnv env = PracticeEnv.INSTANCE;
        int currentVisitId = env.getCurrentVisitId();
        if( currentVisitId > 0 ){
            return currentVisitId;
        }
        int tempVisitId = env.getTempVisitId();
        if( tempVisitId > 0 ){
            return tempVisitId;
        }
        return 0;
    }

}
