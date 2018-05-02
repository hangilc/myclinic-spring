package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    static void removeExtraShinryouFromVisits(List<VisitFull2DTO> visits, ShinryouMasterDTO master, int toBeRemained)
            throws Exception {
        List<ShinryouFullDTO> targets = filterShinryouInVisits(visits,
                s -> s.master.shinryoucode == master.shinryoucode);
        List<Integer> shinryouIds = targets.subList(toBeRemained, targets.size())
                .stream().map(s -> s.shinryou.shinryouId).collect(Collectors.toList());
        boolean success = Service.api.batchDeleteShinryouCall(shinryouIds).execute().body();
        assert success;
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
