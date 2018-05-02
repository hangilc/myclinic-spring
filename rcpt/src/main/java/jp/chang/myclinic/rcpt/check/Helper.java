package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class Helper {

    private static Logger logger = LoggerFactory.getLogger(Helper.class);

    private Helper() { }

    static int countShinryoucodeInVisits(List<VisitFull2DTO> visits, int shinryoucode){
        int n = 0;
        for(VisitFull2DTO visit: visits){
            for(ShinryouFullDTO shinryou: visit.shinryouList){
                if( shinryou.master.shinryoucode == shinryoucode ){
                    n += 1;
                }
            }
        }
        return n;
    }

    static boolean shinryoucodeExistsInVisits(List<VisitFull2DTO> visits, int shinryoucode){
        for(VisitFull2DTO visit: visits){
            for(ShinryouFullDTO shinryou: visit.shinryouList){
                if( shinryou.master.shinryoucode == shinryoucode ){
                    return true;
                }
            }
        }
        return false;
    }

    static List<ShinryouFullDTO> filterShinryouInVisits(List<VisitFull2DTO> visits, Predicate<ShinryouFullDTO> pred){
        List<ShinryouFullDTO> result = new ArrayList<>();
        for(VisitFull2DTO visit: visits){
            for(ShinryouFullDTO shinryou: visit.shinryouList){
                if( pred.test(shinryou) ){
                    result.add(shinryou);
                }
            }
        }
        return result;
    }

    static List<VisitFull2DTO> filterVisitInVisits(List<VisitFull2DTO> visits, Predicate<VisitFull2DTO> pred){
        List<VisitFull2DTO> result = new ArrayList<>();
        for(VisitFull2DTO visit: visits){
            if( pred.test(visit) ){
                result.add(visit);
            }
        }
        return result;
    }

    static boolean drugExistsInVisits(List<VisitFull2DTO> visits){
        for(VisitFull2DTO visit: visits){
            if( visit.drugs.size() > 0 ){
                return true;
            }
        }
        return false;
    }

}
